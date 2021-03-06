package me.obsilabor.noriskclientbot.discord.listener

import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.channel.editMemberPermission
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.actionRow
import kotlinx.coroutines.launch
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.nrcGuild
import me.obsilabor.noriskclientbot.tickets.Ticket
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

@KordPreview
class ButtonListener : Listener {

    override suspend fun register(client: Kord) {
        client.on<ButtonInteractionCreateEvent> {
            val interaction = this.interaction
            for(ticket in MongoDatabase.tickets.find()) {
                if((interaction).componentId == ticket.closeButtonId) {
                    val tckit = MongoDatabase.tickets.findOne { Ticket::closeButtonId eq (interaction).componentId }
                    val channel = tckit?.channelId?.let { nrcGuild.getChannel(it) }
                    MongoDatabase.tickets.deleteOne(Ticket::closeButtonId eq (interaction).componentId)
                    channel?.delete("Closed by ${interaction.member().username}")
                }
            }
            if((interaction).componentId == "TICKET") {
                var created = false
                for (ticket in MongoDatabase.tickets.find()) {
                    if(ticket.member == this.interaction.member().id) {
                        created = true
                    }
                }
                if(!created) {
                    val id = MongoDatabase.tickets.find().toList().size+1
                    val channel = nrcGuild.createTextChannel("Ticket #$id") {
                        parentId = Snowflake(ConfigManager.noRiskClientBotConfig.supportCategoryId ?: "supportCategoryId is null!")
                    }
                    channel.editMemberPermission(interaction.member().id) {
                        allowed = allowed.plus(Permission.ReadMessageHistory)
                        allowed = allowed.plus(Permission.SendMessages)
                        allowed = allowed.plus(Permission.ViewChannel)
                    }
                    channel.createMessage {
                        content = "A staff member will help you soon, please describe your problem as detailed as possible ${interaction.member().mention}!"
                        actionRow {
                            interactionButton(
                                style = ButtonStyle.Danger,
                                customId = "close_$id"
                            ) {
                                label = "Close ticket"
                                emoji = DiscordPartialEmoji(name = Emojis.noEntry.unicode)
                            }
                        }
                    }
                    MongoDatabase.mongoScope.launch {
                        MongoDatabase.tickets.insertOne(Ticket(
                            channel.id,
                            "close_$id",
                            interaction.member().id
                        ))
                    }
                    interaction.acknowledgeEphemeral().followUpEphemeral {
                        content = "Ticket created"
                    }
                } else {
                    interaction.acknowledgeEphemeral().followUpEphemeral {
                        content = "You already have an open ticket!"
                    }
                }
            }
        }
    }
}
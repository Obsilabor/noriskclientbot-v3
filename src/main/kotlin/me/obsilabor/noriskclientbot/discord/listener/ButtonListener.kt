package me.obsilabor.noriskclientbot.discord.listener

import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.channel.editMemberPermission
import dev.kord.core.behavior.channel.editRolePermission
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.interaction.ComponentInteraction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.actionRow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.nrcGuild
import me.obsilabor.noriskclientbot.tickets.Ticket
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

@KordPreview
class ButtonListener : Listener {

    override fun register(client: Kord) {
        client.on<InteractionCreateEvent> {
            val interaction = this.interaction
            if(interaction is ComponentInteraction) {
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
                        channel.editRolePermission(Snowflake(ConfigManager.noRiskClientBotConfig.everyoneRoleId ?: error("everyoneRoleId is null!"))) {
                            allowed.minus(Permission.ReadMessageHistory)
                            allowed.minus(Permission.SendMessages)
                            allowed.minus(Permission.ViewChannel)
                            denied.plus(Permission.ReadMessageHistory)
                            denied.plus(Permission.SendMessages)
                            denied.plus(Permission.ViewChannel)
                        }
                        channel.editRolePermission(Snowflake(ConfigManager.noRiskClientBotConfig.helperRoleId ?: error("helperRoleId is null!"))) {
                            denied.minus(Permission.ReadMessageHistory)
                            denied.minus(Permission.SendMessages)
                            denied.minus(Permission.ViewChannel)
                            allowed.plus(Permission.ReadMessageHistory)
                            allowed.plus(Permission.SendMessages)
                            allowed.plus(Permission.ViewChannel)
                        }
                        channel.editMemberPermission(interaction.member().id) {
                            denied.minus(Permission.ReadMessageHistory)
                            denied.minus(Permission.SendMessages)
                            denied.minus(Permission.ViewChannel)
                            allowed.plus(Permission.ReadMessageHistory)
                            allowed.plus(Permission.SendMessages)
                            allowed.plus(Permission.ViewChannel)
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
}
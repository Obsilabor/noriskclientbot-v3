package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.string
import kotlinx.coroutines.launch
import me.obsilabor.noriskclientbot.data.StringContainer
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions
import org.litote.kmongo.bson
import org.litote.kmongo.json

@KordPreview
object BlacklistCommand : AdvancedCommand(
    commandName = "blacklist",
    commandDescription = "Manage blacklisted words",
    commandCategory = CommandCategory.MODERATION,
    {
        string("action", "What sould be done") {
            required = true
            for (action in BlacklistAction.values()) {
                choice(action.name.lowercase(), action.name.lowercase())
            }
        }
        string("word", "The word to manage") {
            required = false
        }
    }
) {

    override suspend fun handle(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.ManageMessages)) {
            val word = interaction.command.options["word"]?.value
            when(BlacklistAction.valueOf(interaction.command.options["action"]?.value.toString().uppercase())) {
                BlacklistAction.LIST -> {
                    val blacklist = MongoDatabase.blacklist.find().toList().map { it.value }.toList()
                    interaction.acknowledgeEphemeral().followUpEphemeral {
                        content = blacklist.joinToString(", ")
                    }
                }
                BlacklistAction.ADD -> {
                    if(word == null) {
                        interaction.acknowledgePublic().followUp {
                            content = "${interaction.member().mention}, please enter a word to add!"
                        }
                    } else {
                        MongoDatabase.mongoScope.launch {
                            MongoDatabase.blacklist.insertOne(StringContainer(word.toString()))
                        }
                        interaction.acknowledgeEphemeral().followUpEphemeral {
                            content = "${interaction.member().mention}, added `$word` to the blacklist!"
                        }
                    }
                }
                BlacklistAction.REMOVE -> {
                    if(word == null) {
                        interaction.acknowledgePublic().followUp {
                            content = "${interaction.member().mention}, please enter a word to remove!"
                        }
                    } else {
                        MongoDatabase.mongoScope.launch {
                            MongoDatabase.blacklist.deleteOne("{\"value\": \"$word\"}".json.bson)
                        }
                        interaction.acknowledgeEphemeral().followUpEphemeral {
                            content = "${interaction.member().mention}, removed `$word` from the blacklist!"
                        }
                    }
                }
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }

}

@KordPreview
enum class BlacklistAction {

    ADD,
    REMOVE,
    LIST

}
package me.obsilabor.noriskclientbot.discord.command

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.edit
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import me.obsilabor.noriskclientbot.extensions.guild
import me.obsilabor.noriskclientbot.extensions.member

@KordPreview
abstract class SimpleCommand(
    val commandName: String,
    val commandDescription: String
) : Command<CommandInteraction>(
    name = commandName,
    description = commandDescription
) {

    override suspend fun handle(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            content = "_ _"
        }
        handleLegacy(LegacyInteraction(
            null,
            interaction.member(),
            interaction.guild(),
            interaction.channel,
            emptyList()
        ))
    }

    abstract suspend fun handleLegacy(interaction: LegacyInteraction)

    override suspend fun register() {
        @Suppress("LeakingThis")
        CommandManager.register((this))
    }

}
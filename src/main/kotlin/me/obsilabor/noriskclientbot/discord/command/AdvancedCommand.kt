package me.obsilabor.noriskclientbot.discord.command

import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.ApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder

@KordPreview
abstract class AdvancedCommand(
    val commandName: String,
    val commandDescription: String,
    val commandCategory: CommandCategory,
    val builder: ChatInputCreateBuilder.() -> Unit = {}
) : Command<CommandInteraction>(
    name = commandName,
    description = commandDescription,
    category = commandCategory
) {

    override suspend fun register() {
        CommandManager.register((this))
    }

}
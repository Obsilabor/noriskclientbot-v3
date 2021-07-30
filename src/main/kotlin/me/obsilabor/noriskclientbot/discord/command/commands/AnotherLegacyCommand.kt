package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import me.obsilabor.noriskclientbot.discord.command.LegacyInteraction
import me.obsilabor.noriskclientbot.discord.command.SimpleCommand

@KordPreview
object AnotherLegacyCommand : SimpleCommand(
    commandName = "legacy",
    commandDescription = "Just another legacy command"
) {
    override suspend fun handleLegacy(interaction: LegacyInteraction) {
        error("you got nixxed!")
        interaction.channel.createMessage("!command support ;)")
    }
}
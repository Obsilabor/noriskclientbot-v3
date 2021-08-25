package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.string
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions

@KordPreview
object InitCommand : AdvancedCommand(
    commandName = "init",
    commandDescription = "inits the system (this can reset guild data)",
    commandCategory = CommandCategory.SYSTEM,
    {
        string("args", "arguments for system init") {
            required = false
        }
        string("passphrase", "provide the valid passphrase to run this command") {
            required = true
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.Administrator)) {
            val passphrase = interaction.command.options["passphrase"]?.value.toString()
            if(passphrase == (ConfigManager.noRiskClientBotConfig.passphrase ?: error("Passphrase is null"))) {
                NoRiskClientBot.init()
            } else {
                interaction.channel.sendNoPermissions(interaction)
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
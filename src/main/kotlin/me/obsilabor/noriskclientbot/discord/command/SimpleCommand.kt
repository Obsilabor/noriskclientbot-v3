package me.obsilabor.noriskclientbot.discord.command

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.extensions.guild
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.logging.error.Error
import me.obsilabor.noriskclientbot.systems.OperatingSystem
import me.obsilabor.noriskclientbot.systems.SystemInfo
import me.obsilabor.noriskclientbot.utils.Branding
import java.util.*

@KordPreview
abstract class SimpleCommand(
    val commandName: String,
    val commandDescription: String
) : Command<CommandInteraction>(
    name = commandName,
    description = commandDescription
) {

    override suspend fun handle(interaction: CommandInteraction) {
        kotlin.runCatching {
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
        }.onFailure {
            NoRiskClientBot.logger.uploadErrorAndCreateEmbed(
                Error(
                "${Branding.majorVersion} (${Branding.version})",
                Branding.brand,
                SystemInfo(
                    OperatingSystem.running,
                    Runtime.version().toString()
                ),
                Date().toString(),
                "Parsing legacy to advanced command (${name})",
                it
                ),
                interaction.channel
            )
        }
    }

    abstract suspend fun handleLegacy(interaction: LegacyInteraction)

    override suspend fun register() {
        @Suppress("LeakingThis")
        CommandManager.register((this))
    }

}
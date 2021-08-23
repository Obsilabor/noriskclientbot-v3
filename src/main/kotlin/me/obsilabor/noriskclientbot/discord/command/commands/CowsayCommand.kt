package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand

@KordPreview
object CowsayCommand : AdvancedCommand(
    commandName = "cowsay",
    commandDescription = "Let a cow say something (inspired by cowsay linux program)",
    {
        string("text", "the text which should get said") {
            required = true
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        val text = interaction.command.options["text"]?.value.toString()
        interaction.acknowledgePublic().followUp {
            var space = ""
            var line = "----"
            repeat(text.length) {
                line+="-"
                space+=" "
            }
            content = """
                ```
                 $line
                / $text \
                \ $space /
                 $line
                    \   ^__^
                     \  (oo)\_______
                        (__)\       )\/\
                             ||----w |
                             ||     ||
                ```
            """.trimIndent()
        }
    }
}
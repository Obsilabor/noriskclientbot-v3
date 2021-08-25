package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.string
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory

@KordPreview
object CowsayCommand : AdvancedCommand(
    commandName = "cowsay",
    commandDescription = "Let a cow say something (inspired by cowsay linux program)",
    commandCategory = CommandCategory.FUN,
    {
        string("text", "the text which should get said") {
            required = true
        }
    }
) {
    override suspend fun handle(interaction: ChatInputCommandInteraction) {
        val text = interaction.command.options["text"]?.value.toString()
        interaction.acknowledgePublic().followUp {
            var space = ""
            var line = "----"
            repeat(text.length) {
                line+="-"
                space+=" "
            }
            if(text.lowercase().contains("linux")) {
                content = """
                    ```
                        $line
                       / $text   \
                       \ $space   /
                        $line
                           \
                            \
                              .--.
                             |o_o |
                             |:_/ |
                            //   \ \
                           (|     | )
                          /'\_   _/`\
                          \___)=(___/
                    ```
                """.trimIndent()
            } else {
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
}
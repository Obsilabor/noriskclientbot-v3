package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.rest.Image
import me.obsilabor.noriskclientbot.discord.command.LegacyInteraction
import me.obsilabor.noriskclientbot.discord.command.SimpleCommand
import me.obsilabor.noriskclientbot.extensions.guild

@KordPreview
object CapeCommand : SimpleCommand(
    commandName = "cape",
    commandDescription = "Get a description how to upload your custom cape"
) {
    override suspend fun handleLegacy(interaction: LegacyInteraction) {
        interaction.channel.createEmbed {
            title = "Custom Cape"
            color = Color(0, 251, 255)
            footer {
                icon = interaction.guild?.getIconUrl(Image.Format.GIF)!!
                text = interaction.guild.name
            }
            thumbnail {
                url = interaction.guild?.getIconUrl(Image.Format.GIF)!!
            }
            field {
                name = "Folgende Schritte erklären dir wie du korrekt ein Cape einreichst!"
                value = "1. Lade dir zuerst das Template aus " +
                        interaction.guild?.getChannel(Snowflake("774292827524956181"))?.mention +
                        " oder mit /template in " +
                        interaction.guild?.getChannel(Snowflake("774273609467691018"))?.mention +
                        " herunter.\n"+
                        "2. Bearbeite das Cape mit Programmen wie z.B paint.net\n" +
                        "3. Gehe auf den Minecraft Server kitpvp.de und gebe /cape ein\n" +
                        "4. Klicke auf den Link im Chat und lade dort dein bearbeitetes Template hoch\n" +
                        "5. Fertig! Habe Geduld und warte bis dein Cape geprüft wurde. In " +
                        interaction.guild?.getChannel(Snowflake("774295768181899325"))?.mention +
                        " kannst du überprüfen, ob dein Cape bereits überprüft wurde"
            }
        }
    }
}
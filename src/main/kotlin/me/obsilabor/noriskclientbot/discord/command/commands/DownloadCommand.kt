package me.obsilabor.noriskclientbot.discord.command.commands

import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.rest.Image
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.discord.command.LegacyInteraction
import me.obsilabor.noriskclientbot.discord.command.SimpleCommand

@KordPreview
object DownloadCommand : SimpleCommand(
    commandName = "download",
    commandDescription = "Get every link for the installation",
    commandCategory = CommandCategory.INFORMATION
) {
    override suspend fun handleLegacy(interaction: LegacyInteraction) {
        interaction.channel.createEmbed {
            title = "Download"
            color = Color(0, 251, 255)
            footer {
                icon = interaction.guild?.getIconUrl(Image.Format.GIF)!!
                text = "${interaction.guild.name} - Thanks @Skill#1337 for english translation :)"
            }
            thumbnail {
                url = interaction.guild?.getIconUrl(Image.Format.GIF)!!
            }
            /*
            field {
                name = "Tutorial-Video"
                value = "https://www.youtube.com/watch?v=4dcUdz2Efdc"
            }
            field {
                name = "Download-Link"
                value = "http://noriskclient.de/downloads/client/latest.jar"
            }
            field {
                name = "Download-Link (1.16.5, No Support)"
                value = "https://noriskclient.de/downloads/1.16.5noriskclientv2.zip"
            }
            field {
                name = "2.0-DevBuild (1.16.5)"
                value = "Klicke [hier](https://workupload.com/file/RzYjDg6v4UK) um dir Devbuilds herunterzuladen.\nMehr Informationen gibt es unter `/tag post devbuilds`"
            }
            field {
                name = "Installer"
                value = "https://noriskclient.de/downloads/installer.jar"
            }
             */
            description = """
                **NORISK CLIENT UNSUPPORTED**
                   
                ${Emojis.flagDe} **DEUTSCH // GERMAN**

                Vielleicht habt ihr es mitbekommen oder auch nicht, aber ich bin nun **Badlion Client Partner** ${Emojis.handshake}

                Deswegen wird der Client solange wie ich Partner bin nicht weitergemacht (**6-12 Monate**) ${Emojis.sob} 

                Aber trotzdem dickes dankeschön an alle die den Client ausprobiert haben ${Emojis.heart}

                Wer sich mein Badlion Cape holen möchte, kann er es gerne mit dem **code** "**norisk**" machen um **10%** zu sparen

                https://store.badlion.net/shop/NoRisk

                He She It Quit.
                
                ${Emojis.flagGb} **ENGLISH**
                
                Maybe you already know about this, but as of right now I'm a **Badlion client partner**. ${Emojis.handshake}

                That's why the client won't be supported nor updated in the time I'm a Badlion client partner **(6-12 months)**. ${Emojis.sob}

                However, big thanks anyway to anyone that tried out the client. ${Emojis.heart}

                If you want to get my Badlion cape, you can do so with the **code** **"norisk"** to save **10%**.

                https://store.badlion.net/shop/NoRisk

                He She It Quit. 

            """.trimIndent()
        }
    }
}
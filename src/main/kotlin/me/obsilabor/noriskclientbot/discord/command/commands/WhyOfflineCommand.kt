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
object WhyOfflineCommand : SimpleCommand(
    commandName = "whyoffline",
    commandDescription = "Shows reasons why the client is offline",
    commandCategory = CommandCategory.INFORMATION
) {
    override suspend fun handleLegacy(interaction: LegacyInteraction) {
        interaction.channel.createEmbed {
            title = "Why is the client offline?"
            color = Color(0, 251, 255)
            footer {
                icon = interaction.guild?.getIconUrl(Image.Format.GIF)!!
                text = interaction.guild.name
            }
            thumbnail {
                url = interaction.guild?.getIconUrl(Image.Format.GIF)!!
            }
            description = """
                **WHY IS THE CLIENT OFFLINE // WHY YOU SHOULDN'T USE IT**
                   
                NoRisk is badlion partner: 
                https://discord.com/channels/774271756549619722/774271756549619725/853724203223220264 (for english translation type /download)
                
                The client is very insecure:
                https://discord.com/channels/774271756549619722/774271756549619725/918871855748767784
                
                The servers are offline:
                try it yourself (don't do it because the domain is no longer owned by us and may be sketchy)
                also, they burned down ${Emojis.flame}

            """.trimIndent()
        }
    }
}
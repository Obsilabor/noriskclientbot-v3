package me.obsilabor.noriskclientbot.discord.command.commands

import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.discord.command.CommandManager
import me.obsilabor.noriskclientbot.extensions.guild

@KordPreview
object HelpCommand : AdvancedCommand(
    commandName = "help",
    commandDescription = "Shows help",
    commandCategory = CommandCategory.INFORMATION, {
        string("category", "Select a category to show help about") {
            required = false
            for (category in CommandCategory.values()) {
                choice(category.name.lowercase(), category.name.lowercase())
            }
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        val input = interaction.command.options["category"]?.value
        if(input == null) {
            interaction.acknowledgePublic().followUp {
                embed {
                    title = "Help - Categories"
                    color = Color(0, 251, 255)
                    footer {
                        icon = interaction.guild().getIconUrl(Image.Format.GIF)!!
                        text = interaction.guild().name
                    }
                    thumbnail {
                        url = interaction.guild().getIconUrl(Image.Format.GIF)!!
                    }
                    for(category in CommandCategory.values()) {
                        field {
                            name = category.capitalizedName
                            value = "**${category.capitalizedName}** ${category.emoji}\n__ __\n${category.description}\n__ __\n Type `/help ${category.name.lowercase()}` to view commands of **${category.capitalizedName}**"
                            inline = true
                        }
                    }
                }
            }
        } else {
            val category = CommandCategory.valueOf(input.toString().uppercase())
            interaction.acknowledgePublic().followUp {
                embed {
                    title = "Help - ${category.capitalizedName}"
                    color = Color(0, 251, 255)
                    footer {
                        icon = interaction.guild().getIconUrl(Image.Format.GIF)!!
                        text = interaction.guild().name
                    }
                    thumbnail {
                        url = interaction.guild().getIconUrl(Image.Format.GIF)!!
                    }
                    val yesEmoji = Emojis.blackLargeSquare
                    val noEmoji = Emojis.regionalIndicatorX
                    var string = "${category.emoji} Commands of **${category.capitalizedName}**:\n__ __\n"
                    for(command in CommandManager.getAllCommands()) {
                        if(command.category == category) {
                            string+="${if(command is AdvancedCommand) "$yesEmoji" else "$noEmoji"} `/${command.name}` - ${command.description}\n"
                        }
                    }
                    string+= "\n__ __\n> *Commands marked with $noEmoji are legacy commands and can be executed with `!`*"
                    description = string
                }
            }
        }
    }
}
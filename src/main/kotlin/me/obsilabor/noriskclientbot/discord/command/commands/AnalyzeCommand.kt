package me.obsilabor.noriskclientbot.discord.command.commands

import au.com.origma.perspectiveapi.v1alpha1.models.AttributeType
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.Image
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.create.embed
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.data.*
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.*

@KordPreview
object AnalyzeCommand : AdvancedCommand(
    commandName = "analyze",
    commandDescription = "Takes a comment and analyzes the toxicity of it",
    commandCategory = CommandCategory.MODERATION,
    {
        string("comment", "The comment to analyze") {
            required = true
        }
    }
) {

    override suspend fun handle(interaction: ChatInputCommandInteraction) {
        if(interaction.member().hasPermission(Permission.Administrator)) {
            val comment = interaction.command.options["comment"]?.value.toString().replace("\"", "'")
            interaction.acknowledgePublic().followUp {
                embed {
                    title = "Toxicity Report"
                    color = Color(0, 251, 255)
                    footer {
                        icon = interaction.guild().getIconUrl(Image.Format.GIF)!!
                        text = interaction.guild().name
                    }
                    thumbnail {
                        url = interaction.guild().getIconUrl(Image.Format.GIF)!!
                    }
                    description = "This comment has a toxicity of `${NoRiskClientBot.perspectiveApi.analyze(comment).attributeScores[AttributeType.TOXICITY]?.summaryScore?.value}%`"
                }
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
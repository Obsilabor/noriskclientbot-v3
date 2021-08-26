package me.obsilabor.noriskclientbot.discord.command.commands

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
import io.ktor.http.*
import kotlinx.coroutines.launch
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

    private val httpClient = HttpClient(CIO) {
        expectSuccess = false
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    override suspend fun handle(interaction: ChatInputCommandInteraction) {
        if(interaction.member().hasPermission(Permission.Administrator)) {
            val comment = interaction.command.options["comment"]?.value.toString().replace("\"", "'")
            MongoDatabase.mongoScope.launch {
                val response = httpClient.request<ToxicityResult>("https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=${ConfigManager.noRiskClientBotConfig.perspectiveApiKey ?: error("perspectiveApiKey is null!")}") {
                    contentType(ContentType.Application.Json)
                    body = Request(
                        RequestComponent(
                            TextComponent(comment),
                            arrayListOf("de"),
                            AttributeScores(Toxicity(null, null))
                        )
                    )
                }
                println(response.attributeScores.TOXICITY.summaryScore?.value)
            }
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
                    description = "Look in console"
                }
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
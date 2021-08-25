package me.obsilabor.noriskclientbot

import com.gitlab.kordlib.kordx.emoji.Emojis
import com.gitlab.kordlib.kordx.emoji.toReaction
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordEmoji
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Guild
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.discord.command.CommandManager
import me.obsilabor.noriskclientbot.discord.command.commands.*
import me.obsilabor.noriskclientbot.discord.listener.LegacyCommandListener
import me.obsilabor.noriskclientbot.discord.listener.MessageListener
import me.obsilabor.noriskclientbot.logging.Logger

@KordPreview
suspend fun main() {
    NoRiskClientBot.start()
}

object NoRiskClientBot {

    lateinit var client: Kord; private set

    lateinit var nrcGuild: Guild

    val logger = Logger(System.out)

    @KordPreview
    suspend fun start() {
        println("Starting...")
        client = Kord(ConfigManager.noRiskClientBotConfig.token ?: writeDefaultsAndExit())
        nrcGuild = client.getGuild(Snowflake(ConfigManager.noRiskClientBotConfig.noriskClientGuildId ?: writeDefaultsAndExit()))!!
        MongoDatabase.openConnection()
        DownloadCommand.register()
        RandomCommand.register()
        CapeCommand.register()
        BanCommand.register()
        WarnCommand.register()
        ListWarnsCommand.register()
        KickCommand.register()
        MuteCommand.register()
        UnmuteCommand.register()
        CowsayCommand.register()
        HelpCommand.register()
        BlacklistCommand.register()
        InitCommand.register()
        CommandManager.init()
        MessageListener().register(client)
        LegacyCommandListener().register(client)
        client.login {
            playing("\uD83D\uDC99 NoRiskClient")
        }
    }

    fun writeDefaultsAndExit(): String {
        ConfigManager.ConfigFile("./noriskclientbot.conf").writeText("""
            token: insert_token_here
            mongoConfig {
                host=localhost
                port=27017
                database=database
                username=admin
                password=password
            }
            wastebinClientConfig {
                token=insert_token_here
                serverHostName="https://example.com"
                serverPort=1870
            }
            noriskClientGuildId: id
            logChannelId: id
            mutedRoleId: id
            emoteServerConfig {
                guildId=id
                obsiEmoteId=id
                twelveEmoteId=id
            }
            ticketChannelId: id
            passphrase: ${System.getProperty("user.name")}
            supportChannelId: id
            faqChannelId: id
            supportCategoryId: id
            everyoneRoleId: id
            helperRoleId: id
        """.trimIndent())
        error("Configure the application before running it")
    }

    @KordPreview
    suspend fun init() {
        val ticketChannel = nrcGuild.getChannel(Snowflake(ConfigManager.noRiskClientBotConfig.ticketChannelId ?: error("ticketChannelId is null!"))) as MessageChannelBehavior
        ticketChannel.createMessage {
            embed {
                title = "Ticket Support"
                color = Color(0, 251, 255)
                footer {
                    icon = nrcGuild.getIconUrl(Image.Format.GIF)!!
                    text = nrcGuild.name
                }
                thumbnail {
                    url = nrcGuild.getIconUrl(Image.Format.GIF)!!
                }
                description = "Here you can create a ticket to get support for **complicated problems**.\n__ __\n__ __\n${Emojis.warning} Please be aware that this is **only** for complicated problems that can't be solved in ${nrcGuild.getChannel(Snowflake(ConfigManager.noRiskClientBotConfig.faqChannelId ?: "faqChannelId is null")).mention} or ${nrcGuild.getChannel(Snowflake(ConfigManager.noRiskClientBotConfig.supportChannelId ?: "supportChannelId is null")).mention}\n__ __\n__ __\n> So if you still need help, press the button below to create a ticket!"
            }
            actionRow {
                interactionButton(
                    style = ButtonStyle.Secondary,
                    customId = "TICKET"
                ) {
                    label = "Open a ticket"
                    emoji = DiscordPartialEmoji(name = Emojis.incomingEnvelope.unicode)
                }
            }
        }
    }

}
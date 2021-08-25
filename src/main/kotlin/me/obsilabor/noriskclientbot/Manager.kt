package me.obsilabor.noriskclientbot

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
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
        """.trimIndent())
        error("Configure the application before running it")
    }

}
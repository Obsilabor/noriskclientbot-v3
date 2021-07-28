package me.obsilabor.noriskclientbot

import com.typesafe.config.ConfigFactory
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.entity.Guild
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import me.obsilabor.noriskclientbot.config.ConfigManager

@KordPreview
suspend fun main() {
    NoRiskClientBot.start()
}

object NoRiskClientBot {

    lateinit var client: Kord; private set

    lateinit var nrcGuild: Guild

    @KordPreview
    suspend fun start() {
        println("Starting...")
        client = Kord(ConfigManager.noRiskClientBotConfig.token ?: writeDefaultsAndExit())
        nrcGuild = client.getGuild(Snowflake(ConfigManager.noRiskClientBotConfig.noriskClientGuildId ?: writeDefaultsAndExit()))!!
        client.on<ReadyEvent> {
            val jo = nrcGuild.getChannel(Snowflake("774273183804948500")) as MessageChannelBehavior
            jo.createMessage("hi bin back")
        }
        client.login()
    }

    private fun writeDefaultsAndExit(): String {
        ConfigManager.ConfigFile("./noriskclientbot.conf").writeText("""
            token: insert_token_here
            mongoConfig {
                host: localhost
                port: 27017
                database: database
                username: admin
                password: password
            }
            noriskClientGuildId: id
        """.trimIndent())
        error("Configure the application before running it")
    }

}
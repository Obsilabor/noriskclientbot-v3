package me.obsilabor.noriskclientbot.logging

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.extensions.nrcGuild
import me.obsilabor.noriskclientbot.http.WastebinClient
import me.obsilabor.noriskclientbot.logging.error.Error
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

class Logger(val out: PrintStream) {

    suspend fun debug(message: String): String {
        return log(Level.DEBUG, message)
    }

    suspend fun info(message: String): String {
        return log(Level.INFO, message)
    }

    suspend fun warn(message: String): String {
        return log(Level.WARN, message)
    }

    suspend fun error(message: String): String {
        return log(Level.ERROR, message)
    }

    suspend fun fatal(message: String): String {
        return log(Level.FATAL, message)
    }
    suspend fun fatal(message: String, exception: Exception): String {
        val returned = log(Level.FATAL, message)
        exception.printStackTrace()
        return returned
    }

    suspend fun uploadError(error: Error): String {
        fatal("${error.context}: \n${error.throwable.stackTraceToString()}")
        return WastebinClient.runClient(arrayOf("""
        Date: ${error.date}
        Context: ${error.context}
        Thread: ${Thread.currentThread()}
        Bot attributes:
            Version: ${error.botVersion}
            Brand: ${error.botBrand}
        System info:
            OS: ${error.system.operatingSystem}
            Java Version: ${error.system.javaVersion}
        Stacktrace: 
        ${error.throwable.stackTraceToString()}
        """.trimIndent())) ?: "FAILURE XD"
    }

    suspend fun uploadErrorAndCreateEmbed(error: Error, channel: MessageChannelBehavior) {
        val url = uploadError(error)
        channel.createEmbed {
            title = ":warning: **A critical error occurred**"
            description = "Send [this]($url) link to an developer of the bot"
        }
    }

    suspend fun log(level: Level, message: String): String {
        return if(level.isEnabled) {
            val string = "[${SimpleDateFormat("HH:mm:ss").format(Date())}] [${Thread.currentThread().threadGroup.name}/${level.name}]: $message"
            (nrcGuild.getChannel(Snowflake(ConfigManager.noRiskClientBotConfig.logChannelId ?: error("No log channel set!"))) as MessageChannelBehavior).createMessage(string)
            out.println(string)
            message
        } else {
            "The logger level ${level.name} is disabled"
        }
    }

}
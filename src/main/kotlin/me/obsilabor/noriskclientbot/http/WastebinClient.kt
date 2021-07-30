package me.obsilabor.noriskclientbot.http

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.config.ConfigManager

object WastebinClient {

    suspend fun runClient(args: Array<String>): String? {
        if(args.isNotEmpty()) {
            val text = args.joinToString(" ")
            val config = ConfigManager.noRiskClientBotConfig.wastebinClientConfig!!
            if(config.token == null || config.serverHostName == null || config.serverPort == null) {
                println("wastebin: No token set")
                NoRiskClientBot.writeDefaultsAndExit()
            }
            val httpClient = HttpClient(CIO) {
                expectSuccess = false
            }
            val response = httpClient.post<HttpResponse>("${config.serverHostName}:${config.serverPort}/create?token=${config.token}&text=${text}")
            val url = response.receive<String?>()
            if(response.status.isSuccess()) {
                println("Wasted! $url")
            } else {
                println("wastebin: ${response.status.value}: ${response.status.description} ${url ?: ""}")
            }
            return url
        } else {
            println("wastebin: No text given")
        }
        return null
    }

}


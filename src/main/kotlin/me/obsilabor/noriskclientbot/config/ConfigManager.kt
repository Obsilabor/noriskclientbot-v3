package me.obsilabor.noriskclientbot.config

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import java.io.File

object ConfigManager {

    class ConfigFile(path: String) : File(path) {
        init {
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            if (!exists()) {
                createNewFile()
            }
        }
    }

    val noRiskClientBotConfig = ConfigFactory.parseFile(ConfigFile("./noriskclientbot.conf")).extract<NoRiskClientBotConfig>()

}
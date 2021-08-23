package me.obsilabor.noriskclientbot.database

import com.mongodb.client.MongoCollection
import me.obsilabor.noriskclientbot.NoRiskClientBot.logger
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.data.MemberInfo
import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.database.mongodb.MongoDB

object MongoDatabase {

    suspend fun openConnection() {
        logger.debug("Creating mongodb connection...")
        mongoDB = MongoDB(
            DatabaseLoginInformation(
                ConfigManager.noRiskClientBotConfig.mongoConfig?.host ?: error(logger.fatal("MongoDB login information cannot be null")),
                ConfigManager.noRiskClientBotConfig.mongoConfig.port ?: error(logger.fatal("MongoDB login information cannot be null")),
                ConfigManager.noRiskClientBotConfig.mongoConfig.database ?: error(logger.fatal("MongoDB login information cannot be null")),
                ConfigManager.noRiskClientBotConfig.mongoConfig.username ?: error(logger.fatal("MongoDB login information cannot be null")),
                ConfigManager.noRiskClientBotConfig.mongoConfig.password?.replace("\"", "") ?: error(logger.fatal("MongoDB login information cannot be null")),
            )
        )
        memberInfo = mongoDB.getCollectionOrCreate<MemberInfo>("NRC_3_memberInfo")
        logger.debug("Created mongodb connection.")

    }

    lateinit var mongoDB: MongoDB

    lateinit var memberInfo: MongoCollection<*>

}
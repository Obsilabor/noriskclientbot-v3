package me.obsilabor.noriskclientbot.database

import com.mongodb.client.MongoCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.obsilabor.noriskclientbot.NoRiskClientBot.logger
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.data.MemberInfo
import me.obsilabor.noriskclientbot.data.StringContainer
import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.database.mongodb.MongoDB

object MongoDatabase {

    val mongoScope = CoroutineScope(Dispatchers.IO)

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
        memberInfo = mongoDB.getCollectionOrCreate("NRC_3_memberInfo")
        blacklist = mongoDB.getCollectionOrCreate("NRC_3_blacklist")
        logger.debug("Created mongodb connection.")

    }

    lateinit var mongoDB: MongoDB

    lateinit var memberInfo: MongoCollection<MemberInfo>
    lateinit var blacklist: MongoCollection<StringContainer>

}
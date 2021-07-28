package me.obsilabor.noriskclientbot.config

import kotlinx.serialization.Serializable

@Serializable
class NoRiskClientBotConfig(
    val token: String? = null,
    val mongoConfig: MongoConfig? = null,
    val noriskClientGuildId: String? = null
)

@Serializable
class MongoConfig(
    val host: String? = null,
    val port: Int? = null,
    val database: String? = null,
    val username: String? = null,
    val password: String? = null
)
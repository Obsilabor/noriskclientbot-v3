package me.obsilabor.noriskclientbot.config

import kotlinx.serialization.Serializable

@Serializable
class NoRiskClientBotConfig(
    val token: String? = null,
    val mongoConfig: MongoConfig? = null,
    val wastebinClientConfig: WastebinClientConfig? = null,
    val noriskClientGuildId: String? = null,
    val logChannelId: String? = null,
    val mutedRoleId: String? = null
)

@Serializable
class MongoConfig(
    val host: String? = null,
    val port: Int? = null,
    val database: String? = null,
    val username: String? = null,
    val password: String? = null
)

@Serializable
class WastebinClientConfig(
    val token: String? = null,
    val serverHostName: String? = null,
    val serverPort: Int? = null
)
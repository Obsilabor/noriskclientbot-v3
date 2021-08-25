package me.obsilabor.noriskclientbot.tickets

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(val channelId: Snowflake, val closeButtonId: String, val member: Snowflake)

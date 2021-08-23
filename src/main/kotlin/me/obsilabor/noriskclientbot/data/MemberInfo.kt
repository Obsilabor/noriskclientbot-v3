package me.obsilabor.noriskclientbot.data

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberInfo(
    @SerialName("_id") val id: Snowflake,
    var warns: ArrayList<Warn>,
    var connectedMinecraftAccount: MinecraftAccount?,
)

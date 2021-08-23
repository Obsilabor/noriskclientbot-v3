package me.obsilabor.noriskclientbot.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberInfo(
    @SerialName("_id") val id: String,
    var warns: ArrayList<Warn>,
    var connectedMinecraftAccount: MinecraftAccount?,
)

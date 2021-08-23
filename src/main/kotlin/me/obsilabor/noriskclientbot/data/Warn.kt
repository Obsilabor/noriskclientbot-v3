package me.obsilabor.noriskclientbot.data

import kotlinx.serialization.Serializable

@Serializable
data class Warn(
    val reason: String,
    val timestamp: Long
)

package me.obsilabor.noriskclientbot.logging.error

import me.obsilabor.noriskclientbot.systems.SystemInfo

data class Error(
    val botVersion: String,
    val botBrand: String,
    val system: SystemInfo,
    val date: String,
    val context: String,
    val throwable: Throwable
)
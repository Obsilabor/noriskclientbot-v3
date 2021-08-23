@file:UseSerializers(UUIDSerializer::class)
package me.obsilabor.noriskclientbot.data

import com.github.jershell.kbson.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
data class MinecraftAccount(
   @SerialName("_id") val uuid: UUID
)

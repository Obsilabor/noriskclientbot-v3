package me.obsilabor.noriskclientbot.detection

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.entity.Message
import me.obsilabor.noriskclientbot.extensions.hasPermission

@KordPreview
object UrlDetection {

    val blockedUrlContents = listOf(
        "https://",
        "http://",
        "www."
    )

    suspend fun Message.containsUrl(): Boolean {
        this.getAuthorAsMember()?.let {
            if(it.hasPermission(Permission.EmbedLinks)) {
                return false
            }
        }
        for (blocked in blockedUrlContents) {
            if(content.lowercase().contains(blocked.lowercase())) {
                return true
            }
        }
        return false
    }

}
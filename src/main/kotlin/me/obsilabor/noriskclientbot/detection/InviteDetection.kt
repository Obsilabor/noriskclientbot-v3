package me.obsilabor.noriskclientbot.detection

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.entity.Message
import me.obsilabor.noriskclientbot.extensions.hasPermission

@KordPreview
object InviteDetection {

    val providers = listOf(
        "discord.gg",
        "invite.gg",
        "dsc.gg"
    )

    suspend fun Message.containsInvite(): Boolean {
        this.getAuthorAsMember()?.let {
            if(it.hasPermission(Permission.Administrator)) {
                return false
            }
        }
        for (provider in providers) {
            if(content.lowercase().contains(provider.lowercase())) {
                return true
            }
        }
        return false
    }

}
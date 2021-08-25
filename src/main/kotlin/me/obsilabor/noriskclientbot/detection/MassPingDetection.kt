package me.obsilabor.noriskclientbot.detection

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.entity.Message
import me.obsilabor.noriskclientbot.extensions.hasPermission

@KordPreview
object MassPingDetection {

    suspend fun Message.getPingCount(): Int {
        this.getAuthorAsMember()?.let {
            if(it.hasPermission(Permission.Administrator)) {
                return 0
            }
        }
        var count = 0
        if(content.length > 7) {
            for((i, char) in content.toCharArray().withIndex()) {
                if(char == '!') {
                    if(content.toCharArray()[i-1] == '@') {
                        if(content.toCharArray()[i-2] == '<') {
                            count++
                        }
                    }
                }
            }
        }
        return count
    }
}
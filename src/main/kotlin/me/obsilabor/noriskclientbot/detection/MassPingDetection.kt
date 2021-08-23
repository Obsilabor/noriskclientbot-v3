package me.obsilabor.noriskclientbot.detection

import dev.kord.core.entity.Message

object MassPingDetection {

    fun Message.getPingCount(): Int {
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
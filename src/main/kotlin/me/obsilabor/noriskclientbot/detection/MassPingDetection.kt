package me.obsilabor.noriskclientbot.detection

import dev.kord.core.entity.Message

object MassPingDetection {

    fun Message.getPingCount(): Int {
        val toCount = "<@!"
        val temp: List<String> = content.split(" ")
        var count = 0
        for (i in temp.indices) {
            if (toCount == temp[i]) count++
        }
        return count;
    }
}
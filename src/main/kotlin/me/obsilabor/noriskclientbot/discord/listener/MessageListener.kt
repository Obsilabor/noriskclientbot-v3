package me.obsilabor.noriskclientbot.discord.listener

import dev.kord.core.Kord
import dev.kord.core.behavior.ban
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import me.obsilabor.noriskclientbot.detection.MassPingDetection.getPingCount

class MessageListener : Listener {

    override fun register(client: Kord) {
        client.on<MessageCreateEvent> {
            if(this.message.getPingCount() > 2) {
                this.message.delete()
                this.member?.ban {
                    deleteMessagesDays = 7
                    reason = "Massive pings"
                }
            }
        }
    }
}
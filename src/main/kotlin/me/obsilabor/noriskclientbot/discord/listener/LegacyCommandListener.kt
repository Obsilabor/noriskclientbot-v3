package me.obsilabor.noriskclientbot.discord.listener

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

class LegacyCommandListener : Listener {

    override fun register(client: Kord) {
        client.on<MessageCreateEvent> {
            if(this.message.content.startsWith("!")) {

            }
        }
    }

}
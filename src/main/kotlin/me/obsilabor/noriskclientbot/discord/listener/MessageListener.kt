package me.obsilabor.noriskclientbot.discord.listener

import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.kord.core.Kord
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.on
import dev.kord.rest.route.Route
import me.obsilabor.noriskclientbot.detection.MassPingDetection.getPingCount

class MessageListener : Listener {

    override fun register(client: Kord) {
        client.on<MessageCreateEvent> {
            if(this.message.getPingCount() > 4) {
                this.message.channel.createMessage("Stop mass pinging!")
                this.message.delete()
                this.member?.ban {
                    deleteMessagesDays = 7
                    reason = "Massive pings"
                }
            }
            if(this.message.content.contains("xD")) {
                this.message.addReaction(ReactionEmoji.Unicode(Emojis.one.unicode))
                this.message.addReaction(ReactionEmoji.Unicode(Emojis.two.unicode))
            }
        }
    }
}
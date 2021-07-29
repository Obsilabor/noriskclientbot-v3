package me.obsilabor.noriskclientbot.discord.listener

import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import me.obsilabor.noriskclientbot.discord.command.CommandManager
import me.obsilabor.noriskclientbot.discord.command.LegacyInteraction

@KordPreview
class LegacyCommandListener : Listener {

    override fun register(client: Kord) {
        client.on<MessageCreateEvent> {
            if(this.message.content.startsWith("!")) {
                val otherCommands = CommandManager.otherCommands
                for (command in otherCommands.keys) {
                    if(this.message.content.startsWith("!$command")) {
                        otherCommands[command]?.handleLegacy(LegacyInteraction(
                            this.message,
                            this.member,
                            this.message.getGuildOrNull(),
                            this.message.channel,
                            this.message.content.split(" ")
                        ))
                        break
                    }
                }
                val slashCommands = CommandManager.slashCommands
                for (command in slashCommands.keys) {
                    if(this.message.content.startsWith("!$command")) {
                        this.member?.let {
                            this.message.channel.createMessage("${it.mention} you tried to run an slash command as an legacy command. Please run commands using the Slash function of discord")
                        }
                        break
                    }
                }
            }
        }
    }

}
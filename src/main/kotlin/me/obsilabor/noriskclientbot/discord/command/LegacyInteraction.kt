package me.obsilabor.noriskclientbot.discord.command

import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message

data class LegacyInteraction(
    val message: Message?,
    val member: Member?,
    val guild: Guild?,
    val channel: MessageChannelBehavior,
    val args: List<String>
)

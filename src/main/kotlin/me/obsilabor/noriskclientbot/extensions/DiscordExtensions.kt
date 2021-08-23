package me.obsilabor.noriskclientbot.extensions

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.Image
import me.obsilabor.noriskclientbot.NoRiskClientBot

val client: Kord
    get() = NoRiskClientBot.client

val nrcGuild: Guild
    get() = NoRiskClientBot.nrcGuild

@KordPreview
suspend fun Interaction.guild(): Guild {
    return kord.getGuild(data.guildId.value!!)!!
}

@KordPreview
suspend fun Interaction.member(): Member  {
    return guild().getMember(data.member.value!!.userId)
}

@KordPreview
suspend fun Member.hasPermission(permission: Permission): Boolean {
    return getPermissions().contains(permission) || getPermissions().contains(Permission.Administrator)
}

suspend fun MessageChannelBehavior.sendNoPermissions(member: Member) {
    createEmbed {
        color = Color(255, 0, 0)
        footer {
            icon = nrcGuild.getIconUrl(Image.Format.GIF)!!
            text = nrcGuild.name
        }
        footer {
            icon = nrcGuild.getIconUrl(Image.Format.GIF)!!
            text = nrcGuild.name
        }
        description = "${member.mention}, sorry but you don't have permission to perform that action!"
    }
}

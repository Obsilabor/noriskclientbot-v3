package me.obsilabor.noriskclientbot.extensions

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.launch
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.data.MemberInfo
import me.obsilabor.noriskclientbot.data.Warn
import me.obsilabor.noriskclientbot.database.MongoDatabase
import org.litote.kmongo.bson
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.json

val client: Kord
    get() = NoRiskClientBot.client

val nrcGuild: Guild
    get() = NoRiskClientBot.nrcGuild

suspend fun emojiGuild(): Guild {
    return client.getGuild(Snowflake(ConfigManager.noRiskClientBotConfig.emoteServerConfig.guildId ?: error("Emoji server id cannot be null")))!!
}

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
        thumbnail {
            url = nrcGuild.getIconUrl(Image.Format.GIF)!!
        }
        description = "${member.mention}, sorry but you don't have permission to perform that action!"
    }
}

@KordPreview
suspend fun MessageChannelBehavior.sendNoPermissions(interaction: Interaction) {
    interaction.acknowledgeEphemeral().followUpEphemeral {
        embed {
            color = Color(255, 0, 0)
            footer {
                icon = nrcGuild.getIconUrl(Image.Format.GIF)!!
                text = nrcGuild.name
            }
            thumbnail {
                url = nrcGuild.getIconUrl(Image.Format.GIF)!!
            }
            description = "${interaction.member().mention}, sorry but you don't have permission to perform that action!"
        }
    }
}

@KordPreview
suspend fun Member.warn(reason: String) {
    val warn = Warn(reason, System.currentTimeMillis())
    val memberInfo = MongoDatabase.memberInfo.findOne { MemberInfo::id eq id.asString }
    MongoDatabase.mongoScope.launch {
        if(memberInfo == null) {
            MongoDatabase.memberInfo.insertOne(
                MemberInfo(
                id.asString,
                arrayListOf(warn),
                null
            )
            )
        } else {
            val warns = memberInfo.warns.clone() as ArrayList<Warn>
            warns.add(warn)
            MongoDatabase.memberInfo.replaceOne(memberInfo.json.bson, MemberInfo(
                id.asString,
                warns,
                memberInfo.connectedMinecraftAccount
            )
            )
        }
    }
    NoRiskClientBot.logger.info("**${username}#${discriminator}** got warned by me for `${reason}`")
    if(MongoDatabase.memberInfo.findOne { MemberInfo::id eq id.asString }!!.warns.size >= 3) {
        ban {
            this.reason = "$reason (3-Warns)"
        }
        MongoDatabase.mongoScope.launch {
            MongoDatabase.memberInfo.replaceOne(memberInfo!!.json.bson, MemberInfo(
                id.asString,
                arrayListOf(),
                memberInfo.connectedMinecraftAccount
            ))
        }
        NoRiskClientBot.logger.info("**${username}#${discriminator}** got banned for `${reason}`")
    }
}


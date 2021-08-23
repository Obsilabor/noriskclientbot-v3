package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.CommandInteraction
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.NoRiskClientBot.logger
import me.obsilabor.noriskclientbot.data.MemberInfo
import me.obsilabor.noriskclientbot.data.Warn
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.extensions.guild
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions
import org.litote.kmongo.bson
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.json

@KordPreview
object WarnCommand : AdvancedCommand(
    commandName = "warn",
    commandDescription = "Warn users that if they continue like this, they will be punished",
    {
        user("member", "The person which should get warned") {
            required = true
        }
        string("reason", "The reason for the warn") {
            required = true
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.KickMembers)) {
            val member = interaction.command.options["member"]?.value as Member
            val reason = interaction.command.options["reason"]?.value as String
            interaction.acknowledgePublic().followUp {
                content = "${member.mention} got warned!"
                val warn = Warn(reason)
                val origin = MongoDatabase.memberInfo.findOne { MemberInfo::id eq member.id }
                var memberInfo = origin
                if(memberInfo == null) {
                    memberInfo = MemberInfo(
                        member.id,
                        arrayListOf(warn),
                        null
                    )
                } else {
                    memberInfo.warns.add(warn)
                    MongoDatabase.memberInfo.deleteOne(origin!!.json.bson)
                }
                MongoDatabase.memberInfo.insertOne(memberInfo)
                logger.debug("Trying to dm ${member.username}#${member.discriminator}")
                kotlin.runCatching {
                    member.getDmChannel().createMessage("You got **warned** on ${interaction.guild().name} for `$reason`!")
                }.onFailure {
                    logger.warn("Couldn't dm ${member.username}#${member.discriminator}")
                }
                logger.info("**${member.username}#${member.discriminator}** got warned by **${interaction.member().username}#${interaction.member().discriminator}** for `${reason}`")
                if(memberInfo.warns.size >= 3) {
                    member.ban {
                        this.reason = "$reason (3-Warns)"
                    }
                    logger.info("**${member.username}#${member.discriminator}** got banned by **${interaction.member().username}#${interaction.member().discriminator}** for `${reason}`")
                }
            }
        } else {
            interaction.channel.sendNoPermissions(interaction.member())
        }
    }
}
package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.CommandInteraction
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.extensions.guild
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions

@KordPreview
object KickCommand : AdvancedCommand(
    commandName = "kick",
    commandDescription = "Kick users from the server!",
    {
        user("member", "The person which should get kicked") {
            required = true
        }
        string("reason", "The reason for the kick") {
            required = false
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.BanMembers)) {
            val member = interaction.command.options["member"]?.value as Member
            val kickReason = interaction.command.options["reason"]?.value
            interaction.acknowledgePublic().followUp {
                kotlin.runCatching {
                    member.getDmChannel().createMessage("You got **kicked** on ${interaction.guild().name}${if(kickReason == null) "!" else " for `${kickReason}`!"}")
                }.onFailure {
                    NoRiskClientBot.logger.warn("Couldn't dm ${member.username}#${member.discriminator}")
                }
                content = "${member.mention} got kicked!"
                member.ban {
                    kickReason?.let {
                        reason = it.toString()
                    }
                }
                NoRiskClientBot.logger.info("**${member.username}#${member.discriminator}** got kicked by **${interaction.member().username}#${interaction.member().discriminator}**${if(kickReason == null) "" else " for `${kickReason}`"}")
            }
        } else {
            interaction.channel.sendNoPermissions(interaction.member())
        }
    }
}
package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.user
import me.obsilabor.noriskclientbot.NoRiskClientBot.logger
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.guild
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions

@KordPreview
object BanCommand : AdvancedCommand(
    commandName = "ban",
    commandDescription = "Ban any user!",
    commandCategory = CommandCategory.MODERATION,
    {
        user("member", "The person which should get banned") {
            required = true
        }
        string("reason", "The reason for the ban") {
            required = false
        }
    }
) {
    override suspend fun handle(interaction: ChatInputCommandInteraction) {
        if(interaction.member().hasPermission(Permission.BanMembers)) {
            val member = interaction.command.options["member"]?.value as Member
            val banReason = interaction.command.options["reason"]?.value
            interaction.acknowledgePublic().followUp {
                kotlin.runCatching {
                    member.getDmChannel().createMessage("You got **banned** on ${interaction.guild().name}${if(banReason == null) "!" else " for `${banReason}`!"}")
                }.onFailure {
                    logger.warn("Couldn't dm ${member.username}#${member.discriminator}")
                }
                content = "${member.mention} got banned!"
                member.ban {
                    banReason?.let {
                        reason = it.toString()
                    }
                }
                logger.info("**${member.username}#${member.discriminator}** got banned by **${interaction.member().username}#${interaction.member().discriminator}**${if(banReason == null) "" else " for `${banReason}`"}")
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
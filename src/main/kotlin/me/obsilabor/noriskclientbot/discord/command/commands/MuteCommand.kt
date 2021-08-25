package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.user
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions

@KordPreview
object MuteCommand : AdvancedCommand(
    commandName = "mute",
    commandDescription = "Mute users so they can't chat anymore",
    commandCategory = CommandCategory.MODERATION,
    {
        user("member", "The person which should get muted") {
            required = true
        }
        string("reason", "The reason for the mute") {
            required = false
        }
    }
) {

    override suspend fun handle(interaction: ChatInputCommandInteraction) {
        if(interaction.member().hasPermission(Permission.MuteMembers)) {
            val member = interaction.command.options["member"]?.value as Member
            val muteReason = interaction.command.options["reason"]?.value
            interaction.acknowledgePublic().followUp {
                content = "${member.mention} got muted!"
                member.addRole(Snowflake(ConfigManager.noRiskClientBotConfig.mutedRoleId ?: error("Muted role id cannot be null!")), reason = muteReason.toString())
                NoRiskClientBot.logger.info("**${member.username}#${member.discriminator}** got muted by **${interaction.member().username}#${interaction.member().discriminator}**${if(muteReason == null) "" else " for `${muteReason}`"}")
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
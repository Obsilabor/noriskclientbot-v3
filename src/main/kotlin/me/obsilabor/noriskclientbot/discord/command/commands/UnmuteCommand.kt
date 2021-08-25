package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.builder.interaction.user
import me.obsilabor.noriskclientbot.NoRiskClientBot
import me.obsilabor.noriskclientbot.config.ConfigManager
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions

@KordPreview
object UnmuteCommand : AdvancedCommand(
    commandName = "unmute",
    commandDescription = "Munute users so they can chat again",
    commandCategory = CommandCategory.MODERATION,
    {
        user("member", "The person which should get unmuted") {
            required = true
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.MuteMembers)) {
            val member = interaction.command.options["member"]?.value as Member
            interaction.acknowledgePublic().followUp {
                content = "${member.mention} got unmuted!"
                member.removeRole(Snowflake(ConfigManager.noRiskClientBotConfig.mutedRoleId ?: error("Muted role id cannot be null!")))
                NoRiskClientBot.logger.info("**${member.username}#${member.discriminator}** got unmuted by **${interaction.member().username}#${interaction.member().discriminator}**")
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
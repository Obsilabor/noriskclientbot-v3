package me.obsilabor.noriskclientbot.discord.command.commands

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.rest.Image
import dev.kord.rest.builder.interaction.user
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.noriskclientbot.data.MemberInfo
import me.obsilabor.noriskclientbot.database.MongoDatabase
import me.obsilabor.noriskclientbot.discord.command.AdvancedCommand
import me.obsilabor.noriskclientbot.discord.command.CommandCategory
import me.obsilabor.noriskclientbot.extensions.guild
import me.obsilabor.noriskclientbot.extensions.hasPermission
import me.obsilabor.noriskclientbot.extensions.member
import me.obsilabor.noriskclientbot.extensions.sendNoPermissions
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import java.text.SimpleDateFormat
import java.util.*

@KordPreview
object ListWarnsCommand : AdvancedCommand(
    commandName = "warns",
    commandDescription = "List the warns of an specific member",
    commandCategory = CommandCategory.MODERATION,
    {
        user("member", "The member whose warnings you want to see") {
            required = true
        }
    }
) {
    override suspend fun handle(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.KickMembers)) {
            val member = interaction.command.options["member"]?.value as Member
            val memberInfo = MongoDatabase.memberInfo.findOne { MemberInfo::id eq member.id.asString }
            interaction.acknowledgePublic().followUp {
                embed {
                    title = "Warns from ${member.username}#${member.discriminator}"
                    color = Color(0, 251, 255)
                    footer {
                        icon = interaction.guild().getIconUrl(Image.Format.GIF)!!
                        text = interaction.guild().name
                    }
                    thumbnail {
                        url = interaction.guild().getIconUrl(Image.Format.GIF)!!
                    }
                    if(memberInfo == null) {
                        description = "This member has no warnings."
                    } else {
                        for (warning in memberInfo.warns) {
                            field {
                                inline = true
                                name = warning.reason
                                value = "Warned on the __${SimpleDateFormat("dd.MM.yyyy").format(Date(warning.timestamp))}__"
                            }
                        }
                    }
                }
            }
        } else {
            interaction.channel.sendNoPermissions(interaction)
        }
    }
}
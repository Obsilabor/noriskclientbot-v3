package me.obsilabor.noriskclientbot.discord.command

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.createChatInputCommand
import dev.kord.core.entity.Guild
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.interaction.ChatInputCommandCreateEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.obsilabor.noriskclientbot.NoRiskClientBot.logger
import me.obsilabor.noriskclientbot.extensions.client
import me.obsilabor.noriskclientbot.logging.error.Error
import me.obsilabor.noriskclientbot.systems.OperatingSystem
import me.obsilabor.noriskclientbot.systems.SystemInfo
import me.obsilabor.noriskclientbot.utils.Branding
import java.util.*
import kotlin.collections.HashMap

@KordPreview
object CommandManager {

    val slashCommands = HashMap<String, AdvancedCommand>()
    val otherCommands = HashMap<String, SimpleCommand>()

    fun getAllCommands(): List<Command<*>> {
        val list = arrayListOf<Command<*>>()
        for(command in slashCommands.values) {
            list.add(command)
        }
        for(command in otherCommands.values) {
            list.add(command)
        }
        return list
    }

    fun register(command: Command<*>) {
        if(command is AdvancedCommand) {
            slashCommands[command.name] = command
        } else {
            otherCommands[command.name] = command as SimpleCommand
        }
    }

    val commandScope = CoroutineScope(Dispatchers.IO)

    suspend fun init() {
        commandScope.launch {
            cleanupGuilds()
        }
        client.guilds.collect {
            logger.info("Registering commands for ${it.name}")
        }
        commandScope.launch {
            logger.debug("Registering on guilds..")
            registerOnGuilds()
            logger.debug("Registered on guilds!")
        }
        client.on<GuildCreateEvent> {
            val guild = this.guild
            commandScope.launch {
                logger.debug("Cleaning up ${guild.name}")
                guild.cleanupCommands()
                logger.debug("Registering commands for ${guild.name}")
                guild.registerCommands()
                guild.editSelfNickname("NoRiskClientBot :3")
                logger.info("${guild.name} is ready")
            }
        }
        client.on<ChatInputCommandInteractionCreateEvent> {
            kotlin.runCatching {
                if(slashCommands.containsKey(interaction.command.rootName)) {
                    slashCommands[interaction.command.rootName]?.handle(interaction)
                } else {
                    otherCommands[interaction.command.rootName]?.handle(interaction)
                }
            }.onFailure {
                logger.uploadErrorAndCreateEmbed(
                    Error(
                        "${Branding.majorVersion} (${Branding.version})",
                        Branding.brand,
                        SystemInfo(
                            OperatingSystem.running,
                            Runtime.version().toString()
                        ),
                        Date().toString(),
                        "Executing advanced command (Interaction: ${interaction})",
                        it
                    ),
                    interaction.channel
                )
            }
        }
        /*
        client.on<InteractionCreateEvent> {
            if(interaction is CommandInteraction) {
                val commandInteraction = interaction as CommandInteraction
                kotlin.runCatching {
                    if(slashCommands.containsKey(commandInteraction.command.rootName)) {
                        slashCommands[(interaction as CommandInteraction).command.rootName]?.handle(interaction as CommandInteraction)
                    } else {
                        otherCommands[(interaction as CommandInteraction).command.rootName]?.handle(interaction as CommandInteraction)
                    }
                }.onFailure {
                    logger.uploadErrorAndCreateEmbed(
                        Error(
                        "${Branding.majorVersion} (${Branding.version})",
                        Branding.brand,
                        SystemInfo(
                            OperatingSystem.running,
                            Runtime.version().toString()
                        ),
                        Date().toString(),
                        "Executing advanced command (Interaction: ${interaction})",
                        it
                    ),
                        interaction.channel
                    )
                }
            }
        }
         */
    }

    suspend fun reloadCommands() {
        commandScope.launch {
            cleanupGuilds()
            registerOnGuilds()
        }
    }

    private suspend fun registerOnGuilds() = client.guilds.collect { it.registerCommands() }

    private suspend fun cleanupGuilds() = client.guilds.collect { it.cleanupCommands() }

    private suspend fun Guild.registerCommands() {
        slashCommands.forEach { commandEntry ->
            val command = commandEntry.value
            createChatInputCommand(command.name, command.description) { command.builder.invoke(this) }
        }
        otherCommands.forEach { commandEntry ->
            val command = commandEntry.value
            createChatInputCommand(command.name, command.description)
        }
    }

    private suspend fun Guild.cleanupCommands() {
        commands.collect { command ->
            if (!slashCommands.containsKey(command.name) && !otherCommands.containsKey(command.name)) {
                command.delete()
            }
        }
    }

}
package me.obsilabor.noriskclientbot.discord.listener

import dev.kord.core.Kord

interface Listener {

    abstract suspend fun register(client: Kord)

}
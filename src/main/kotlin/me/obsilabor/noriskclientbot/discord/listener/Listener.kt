package me.obsilabor.noriskclientbot.discord.listener

import dev.kord.core.Kord

interface Listener {

    abstract fun register(client: Kord)

}
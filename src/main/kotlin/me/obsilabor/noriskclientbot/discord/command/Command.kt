package me.obsilabor.noriskclientbot.discord.command

abstract class Command<T>(
    val name: String,
    val description: String
) {

    abstract suspend fun handle(interaction: T)

    abstract suspend fun register()

}
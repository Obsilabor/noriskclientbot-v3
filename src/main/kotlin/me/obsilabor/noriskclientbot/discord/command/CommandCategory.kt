package me.obsilabor.noriskclientbot.discord.command

import com.gitlab.kordlib.kordx.emoji.Emojis

enum class CommandCategory(val capitalizedName: String, val emoji: String, val description: String) {

    INFORMATION("Information", Emojis.openFileFolder.unicode, "General easy to use commands to show information e.g. how to submit a cape"),
    MODERATION("Moderation", Emojis.rotatingLight.unicode, "Advanced commands for moderators to moderate the server"),
    SYSTEM("System", Emojis.dna.unicode, "High-risk commands integrated directly into the system's core and mostly able to break things"),
    FUN("Fun", Emojis.frog.unicode, "Mostly useless commands to play with e.g. a command to show quotes of survival players");

}
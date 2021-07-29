package me.obsilabor.noriskclientbot.logging

import java.io.PrintStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Logger(val out: PrintStream) {

    fun debug(message: String): String {
        return log(Level.DEBUG, message)
    }

    fun info(message: String): String {
        return log(Level.INFO, message)
    }

    fun warn(message: String): String {
        return log(Level.WARN, message)
    }

    fun error(message: String): String {
        return log(Level.ERROR, message)
    }

    fun fatal(message: String): String {
        return log(Level.FATAL, message)
    }
    fun fatal(message: String, exception: Exception): String {
        val returned = log(Level.FATAL, message)
        exception.printStackTrace()
        return returned
    }

    fun log(level: Level, message: String): String {
        if(level.isEnabled) {
            out.println("[${SimpleDateFormat("hh:MM:ss").format(Date())}] [${Thread.currentThread().threadGroup.name}/${level.name}]: $message")
            return message
        } else {
            return "The logger level ${level.name} is disabled"
        }
    }

}
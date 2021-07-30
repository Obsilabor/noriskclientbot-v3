package me.obsilabor.noriskclientbot.systems

enum class OperatingSystem {

    LINUX,
    WINDOWS,
    MACOS,
    UNKNOWN;

    companion object {
        val running = System.getProperty("os.name").lowercase().let { sysName ->
            when {
                sysName.contains("linux") -> LINUX
                sysName.contains("windows") -> WINDOWS
                sysName.contains("mac") -> MACOS
                else -> UNKNOWN
            }
        }
    }

}
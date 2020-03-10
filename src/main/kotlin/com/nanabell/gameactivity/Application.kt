package com.nanabell.gameactivity

import java.io.File
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files

class Application {

    init {
        val discordService = DiscordService("")
        val commands = Commands(discordService)

        while (run) {
            Thread.sleep(200)
        }
    }

    companion object {
        var run = true
    }
}

fun main() {
    val loader = ClassLoader.getSystemClassLoader() as URLClassLoader
    val method = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
    method.isAccessible = true

    loadLibraries(loader, method)
}

fun loadLibraries(loader: URLClassLoader, method: Method) {
    val libDir = File(Application::class.java.protectionDomain.codeSource.location.path).toPath().resolve("../lib")
    println(libDir)
    if (Files.notExists(libDir)) {
        Files.createDirectory(libDir)
    }

    Files.walk(libDir).filter { Files.isRegularFile(it) }.forEach {
        method.invoke(loader, it.toUri().toURL())
    }
}
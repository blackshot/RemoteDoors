package me.sirhectorin.simpleplugin

import org.bukkit.plugin.java.JavaPlugin
import me.sirhectorin.simpleplugin.utils.Chat.c
import me.sirhectorin.simpleplugin.utils.DB
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class Main : JavaPlugin() {
    val nombre = "SimpleKotlinPlugin"
    companion object {
        lateinit var instance : Main
            private set
    }

    override fun onEnable() {
        instance = this;
        //CREO UNA TABLA SI NO EXISTE ( Y EL ARCHIVO SI ES SQLITE)
        DB().iniciarDb()

        //Register Event Listenerss
        server.pluginManager.registerEvents(EventsListener(this), this)

        //Register Command Executors
        val holacommand = HolaCommand(this)
        getCommand("hola")?.setExecutor(holacommand)
        getCommand("hola")?.tabCompleter = holacommand
        logger.info("&bPlugin cargado &f[&3$nombre&f]".c())
    }

    override fun onDisable() {
    }



}

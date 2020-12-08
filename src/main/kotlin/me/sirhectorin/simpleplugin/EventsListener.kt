package me.sirhectorin.simpleplugin;

import me.sirhectorin.simpleplugin.utils.Chat.c
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class EventsListener(plugin: JavaPlugin) : Listener {
	@EventHandler
	fun onLogin(event: PlayerJoinEvent) {
		val player = event.player
		player.sendMessage("Hola &b${player.name} &f!!".c())
	}
}

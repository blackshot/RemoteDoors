package me.sirhectorin.simpleplugin.utils

import me.sirhectorin.simpleplugin.Main
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Chat {
    val tag = "&4[&9PD&4]&r ".c()

    fun String.c() : String{
        return ChatColor.translateAlternateColorCodes('&', this)
    }

    fun CommandSender.sendColoredMsg(txt : String){
        this.sendMessage(tag + txt.c())
    }
}
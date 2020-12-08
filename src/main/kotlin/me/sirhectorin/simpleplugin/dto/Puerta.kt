package me.sirhectorin.simpleplugin.dto

import org.bukkit.OfflinePlayer

data class Puerta(val x: Int,
                  val y : Int,
                  val z : Int,
                  val player: OfflinePlayer,
                  val mundo : String = "world",
                  var dist : Int = 4,
                  val perm : List<String> = mutableListOf<String>())

{
    val rawPerms: String
        get() = perm.joinToString(separator = " ")

    override fun toString(): String {
        return "&e${player.name}: &7x:&b$x &7y:&b$y &7z:&b$z &6| &9$mundo &6| &7dist: &b$dist &6|&f perms: $rawPerms"
    }
}
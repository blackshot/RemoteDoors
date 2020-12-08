package me.sirhectorin.simpleplugin

import me.sirhectorin.simpleplugin.dao.PuertaRepo
import me.sirhectorin.simpleplugin.dto.Puerta
import me.sirhectorin.simpleplugin.utils.Chat.sendColoredMsg
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class HolaCommand(val plugin: Main) : CommandExecutor, TabCompleter {
	private val subcomandos :List<Pair<String, String>> =
			listOf(
					Pair("list", "Muestra un listado de tus puertas"),
					Pair("create", "Crea una puerta automatica donde estás parado"),
					Pair("remove", "Elimina la puerta automatica en la cual estás parado")
			)

	override fun onCommand(sender: CommandSender, command: Command, label: String, args:  Array<String>) : Boolean {
		// sender.sendColoredMsg("&eSaludos desde &3${this.plugin.description.name}")

		if (args.isEmpty()){
			sender.sendColoredMsg("&e[&6========= &7${this.plugin.description.name} &6=========&e]")
			for( (comando, ayuda) in subcomandos){
				sender.sendColoredMsg("&e/$comando &b- &f$ayuda")
			}
			return true
		}

		when(args[0]){
			"list" -> {
				if(sender is Player){
					val puertas = PuertaRepo.findByOwner(sender.name)
					if (puertas.isEmpty())
						sender.sendColoredMsg("&3No se encontraron puertas")
					else
						puertas.forEach {
							sender.sendColoredMsg(it.toString())
						}
				} else{
					val puertas = PuertaRepo.getAll()
					if (puertas.isEmpty())
						sender.sendColoredMsg("&3No se encontraron puertas")
					else
						puertas.forEach {
							sender.sendColoredMsg(it.toString())
						}
				}
			}
			"create" -> {
				if(sender is Player){
					val p : Player = sender
					if(1 == PuertaRepo.add(Puerta( p.location.blockX, p.location.blockY,
							p.location.blockZ,
							p,
							p.world.name)))
						p.sendColoredMsg("&aPuerta creada exitosamente!")
					else
						p.sendColoredMsg("&cNo se pudo crear la puerta")
				}
			}
			"remove"-> {
				if (sender is Player){
					val p : Player = sender
					val pu = PuertaRepo.getFromLoc(p.location)

					if(pu != null){
						if(pu.player.player != p){
							p.sendColoredMsg("&No eres el dueño de esta puerta!")
							return true
						}
						if(PuertaRepo.delete(pu))
							p.sendColoredMsg("&eBorrado exitosamente!")
						else
							p.sendColoredMsg("&cNo se pudo borrar")
					} else {
						p.sendColoredMsg("&cNo hay ninguna puerta acá")
					}
					return true
				}else{
					sender.sendColoredMsg("&cEste comando es solo para jugadores, no consolas.")
				}
				return true
			}
		}
		return true
	}

	override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String>? {
		val cmds: List<String> = this.subcomandos.map { it.first }

		if (args.size <= 1)
			return cmds.filter { it.startsWith(args[0] ?: "") }

//        if(command.name == "hola")
//            return server.onlinePlayers.map {it.name}
//        else
//            return listOf()
		return emptyList()

	}
}

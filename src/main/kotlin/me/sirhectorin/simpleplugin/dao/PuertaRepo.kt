package me.sirhectorin.simpleplugin.dao

import me.sirhectorin.simpleplugin.dto.Puerta
import me.sirhectorin.simpleplugin.utils.DB
import org.bukkit.Bukkit
import org.bukkit.Location
import java.sql.ResultSet

object PuertaRepo{

    fun add(puerta: Puerta): Int?{
        var ret : Int = -1
        DB().use { c->
            ret = c.update("INSERT INTO puertas (owner, x, y, z, mundo, dist, perm) VALUES (\"${puerta.player.name}\"" +
                    ", ${puerta.x}, ${puerta.y}, ${puerta.z}, \"${puerta.player.player!!.world.name}\", ${puerta.dist}, " +
                    "\"${puerta.rawPerms}\")")
        }
        return ret
    }

    fun getAll(): List<Puerta> {
        val lp = mutableListOf<Puerta>()
        DB().use { db ->
            val rs: ResultSet? = db.query("SELECT * FROM puertas")
            while (rs!!.next()) {
                lp.add(rs.asPuerta())
            }
        }
        return lp
    }
    fun findByOwner(owner : String): List<Puerta>{
        val lp = mutableListOf<Puerta>()
        DB().use { db ->
            val rs: ResultSet? = db.query("SELECT * FROM puertas WHERE owner = \"${owner}\"")
            while (rs!!.next()) {
                lp.add(rs.asPuerta())
            }
        }
        return lp
    }
    fun getFromLoc(loc : Location) : Puerta?{
        DB().use { db->
            val rs = db.query("SELECT * FROM puertas WHERE x = ${loc.blockX} AND y = ${loc.blockY} AND z = ${loc.blockZ} AND mundo = \"${loc.world!!.name}\"")
            return if(rs!!.next()) rs.asPuerta() else null
        }
    }

    fun delete(puerta : Puerta): Boolean{
        DB().use{db->
            return 1 == db.update("DELETE FROM puertas WHERE x = ${puerta.x} AND y = ${puerta.y} AND z = ${puerta.z} AND mundo = \"${puerta.mundo}\"")
        }
    }
    @SuppressWarnings("deprecation")
    private fun ResultSet.asPuerta() : Puerta{
        return Puerta(this.getInt("x"),
                this.getInt("y"),
                this.getInt("z"),
                Bukkit.getOfflinePlayer(this.getString("owner")),this.getString("mundo"),
                this.getInt("dist"), this.getString("perm").split(" "))
    }
}
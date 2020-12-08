package me.sirhectorin.simpleplugin.utils

import me.sirhectorin.simpleplugin.Main
import me.sirhectorin.simpleplugin.utils.Chat.c
import java.sql.*
import java.util.logging.Level

/**
 *
 * @author SirH y compañia xd
 */
class DB() : AutoCloseable {

    private val plugin : Main = Main.instance
    private val dir = this.plugin.dataFolder.toString() + "/"
    private var conexion: Connection? = null
    private var statment: Statement? = null
    private var set: ResultSet? = null
    private val mysql = false // false para usar sqlite (base de datos en archivo sin conexion)
    private var url: String
    private val debug = true // Muestra las consultas SQL que se realizan en la BD
    private val db = "autodoors"
    // CONEXION SQL
    private val ip = "localhost"
    private val puerto = "3306"
    private val user = "testuser"
    private val pw = "testing"

    init {
        url = if (mysql) "jdbc:mysql://$ip:$puerto/$db?user=$user&password=$pw" else "jdbc:sqlite:$dir$db.db"
        try {
            if (mysql) Class.forName("com.mysql.jdbc.Driver") else Class.forName("org.sqlite.JDBC")
            if (!mysql && !this.plugin.dataFolder.exists()) this.plugin.dataFolder.mkdir()
            conexion = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            this.plugin.logger.log(Level.SEVERE, "&3No se puede conectar a la base de datos / archivo".c(), e)
        } catch (ex: ClassNotFoundException) {
            this.plugin.logger.log(Level.SEVERE, null, ex)
        }
    }

    fun query(query: String): ResultSet? {
        try {
            statment = conexion!!.createStatement()
            set = statment!!.executeQuery(query)
            if (debug) println(query)
        } catch (e: SQLException) {
            println("""Excepcion en la consulta [Query]: $query
                ${e.message}""".trimMargin())
        }
        return set
    }

    fun update(update: String): Int {
        val result: Int
        try {
            statment = conexion!!.createStatement()
            result = statment!!.executeUpdate(update)
            if (debug) println(update)
        } catch (e: SQLException) {
            println("""Excepcion en la actualizacion [Update]:
            
            $update
            
            ${e.message}""".trimMargin())

            return if (e.message!!.contains("foreign")) -1 else -2
        }
        return result // si es 0 ningun registro cambió.
    }

    fun iniciarDb(){
        this.use { c->
            c.update("""
            CREATE TABLE IF NOT EXISTS puertas(
                owner VARCHAR not null,
                x INTEGER not NULL,
                y INTEGER NOT NULL,
                z INTEGER NOT NULL,
                mundo VARCHAR NOT NULL,
                dist INTEGER DEFAULT 4,
                perm TEXT,
                PRIMARY key (x, y ,z, owner)
            )""")
            plugin.logger.info("total puertas: " + c.query("SELECT count(*) as total FROM puertas")?.getInt("total"))
        }
    }

    private fun desconectar() {
        try {
            conexion!!.close()
            conexion = null
        } catch (e: SQLException) {
            println("Error al cerrar la conexion")
            e.printStackTrace()
        }
    }

    override fun close() {
        desconectar()
    }
}

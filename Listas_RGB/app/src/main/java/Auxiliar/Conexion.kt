package Auxiliar
import Conexion.AdminSQLIteConexion
import Modelo.Lista
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
object Conexion {
    //Clase tipo Singleton para acceder e métodos sin tener que crear objeto (similar a Static de Java)
    //Si hay algún cambio en la BBDD, se cambia el número de versión y así automáticamente
    // se pasa por el OnUpgrade del AdminSQLite
    //y ahí añades las sentencias que interese modificar de la BBDD
    private  var DATABASE_NAME = "administracion.db3"
    private  var DATABASE_VERSION = 1


    fun cambiarBD(nombreBD:String){
        this.DATABASE_NAME = nombreBD
    }

    fun addLista(contexto: AppCompatActivity, l: Lista):Long{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase //habilito la BBDD para escribir en ella, tambié deja leer.
        val registro = ContentValues() //objeto de kotlin, contenido de valores, un Map. Si haceis ctrl+clic lo veis.
        registro.put("nombre", l.nombre)
        registro.put("mes", l.mes.toString())
        registro.put("dia", l.dia.toString())
        val codigo = bd.insert("listas", null, registro)
        bd.close()
        return codigo
    }

    fun delLista(contexto: AppCompatActivity, nombre: String):Int{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        //val cant = bd.delete("personas", "dni='${dni}'", null)
        val cant = bd.delete("listas", "nombre=?", arrayOf(nombre))
        bd.close()
        return cant
    }
    fun delTodasListas(contexto: AppCompatActivity): Int {
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val cant = bd.delete("listas", null, null) // Elimina todas las filas en la tabla "listas"
        bd.close()
        return cant
    }

    fun buscarLista(contexto: AppCompatActivity, nombre:String):Lista? {
        var l:Lista? = null //si no encuentra ninguno vendrá null, por eso la ? y también en la devolución de la función.
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        /*Esta funciona pero es mejor como está hecho justo debajo con ?
        val fila = bd.rawQuery(
            "select nombre,edad from personas where dni='${dni}'",
            null
        )*/
        val fila =bd.rawQuery(
            "SELECT mes, dia FROM listas WHERE nombre=?",
            arrayOf(nombre)
        )
        //en fila viene un CURSOR, que está justo antes del primero por eso lo ponemos en el primero en la siguiente línea
        if (fila.moveToFirst()) {//si no hay datos el moveToFirst, devuelve false, si hay devuelve true.
            l = Lista(nombre, fila.getInt(0), fila.getInt(1))
        }
        bd.close()
        return l
    }

    fun obtenerListas(contexto: AppCompatActivity):ArrayList<Lista>{
        var listas:ArrayList<Lista> = ArrayList(1)

        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        val fila = bd.rawQuery("select nombre,mes,dia from listas", null)
        while (fila.moveToNext()) {
            var l:Lista = Lista(fila.getString(0),fila.getInt(1),fila.getInt(2))
            listas.add(l)
        }
        bd.close()
        return listas //este arrayList lo puedo poner en un adapter de un RecyclerView por ejemplo.
    }

}
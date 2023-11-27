package Auxiliar
import Conexion.AdminSQLIteConexion
import Conexion.AdminSQLIteConexion2
import Modelo.Nota
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
object Conexion2 {
    //Clase tipo Singleton para acceder e métodos sin tener que crear objeto (similar a Static de Java)
    //Si hay algún cambio en la BBDD, se cambia el número de versión y así automáticamente
    // se pasa por el OnUpgrade del AdminSQLite
    //y ahí añades las sentencias que interese modificar de la BBDD
    private  var DATABASE_NAME = "administracion2.db3"
    private  var DATABASE_VERSION = 1


    fun cambiarBD(nombreBD:String){
        this.DATABASE_NAME = nombreBD
    }

    fun addNota(contexto: AppCompatActivity, n: Nota):Long{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase //habilito la BBDD para escribir en ella, tambié deja leer.
        val registro = ContentValues() //objeto de kotlin, contenido de valores, un Map. Si haceis ctrl+clic lo veis.
        registro.put("producto", n.producto)
        registro.put("cantidad", n.cantidad.toString())
        val codigo = bd.insert("notas", null, registro)
        bd.close()
        return codigo
    }

    fun delNota(contexto: AppCompatActivity, producto: String):Int{
        val admin = AdminSQLIteConexion2(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        //val cant = bd.delete("personas", "dni='${dni}'", null)
        val cant = bd.delete("notas", "producto=?",null)
        bd.close()
        return cant
    }
    fun delTodasNotas(contexto: AppCompatActivity): Int {
        val admin = AdminSQLIteConexion2(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val cant = bd.delete("notas", null, null) // Elimina todas las filas en la tabla "listas"
        bd.close()
        return cant
    }

    fun obtenerNotas(contexto: AppCompatActivity):ArrayList<Nota>{
        var notas:ArrayList<Nota> = ArrayList(1)

        val admin = AdminSQLIteConexion2(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        val fila = bd.rawQuery("select producto,cantidad from notas", null)
        while (fila.moveToNext()) {
            var n:Nota = Nota(fila.getString(0),fila.getInt(1))
            notas.add(n)
        }
        bd.close()
        return notas //este arrayList lo puedo poner en un adapter de un RecyclerView por ejemplo.
    }

}
package Conexion

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AdminSQLiteConexionHuevo(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "puntuacion.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "puntuacion_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PUNTUACION = "puntuacion"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_PUNTUACION INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun actualizarPuntuacion(nuevaPuntuacion: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_PUNTUACION, nuevaPuntuacion)

        // En este ejemplo, actualizo todas las filas, puedes ajustar esto según tus necesidades
        db.update(TABLE_NAME, contentValues, null, null)

        db.close()
    }
    fun getPuntuacion(): Int {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        var puntuacion = 0
        if(cursor.moveToFirst()) {
            puntuacion = cursor.getInt(1)
        }

        cursor.close()
        db.close()

        return puntuacion
    }
}
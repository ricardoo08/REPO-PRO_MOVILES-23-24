package com.example.tresenraya_rgb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tresenraya_rgb.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    var jugadorActual='X'
    var salir=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tablero = mutableMapOf<String, ImageView>()
        tablero["00"] = findViewById(R.id.img11)
        tablero["01"] = findViewById(R.id.img12)
        tablero["02"] = findViewById(R.id.img13)
        tablero["10"] = findViewById(R.id.img21)
        tablero["11"] = findViewById(R.id.img22)
        tablero["12"] = findViewById(R.id.img23)
        tablero["20"] = findViewById(R.id.img31)
        tablero["21"] = findViewById(R.id.img32)
        tablero["22"] = findViewById(R.id.img33)

        for ((_, imageView) in tablero) {
            imageView.setOnClickListener {
                manejarClicCasilla(imageView,tablero)
            }
        }
        binding.btnRnc.setOnClickListener {
            reiniciarJuego(tablero)
        }
    }
    private fun manejarClicCasilla(imageView: ImageView,tablero: Map<String, ImageView>) {
        if (imageView.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.pregunta)?.constantState) {
            // Verifica si la casilla está vacía
            imageView.setImageResource(if (jugadorActual == 'X') {
                R.drawable.milan_
            } else R.drawable.inter_milan)
            // Cambia la imagen del jugador actual

            if (partidaGanada(tablero)) {
                // Verifica si alguien ha ganado
                mostrarGanador()
                reiniciarTablero(tablero)
            } else {
                jugadorActual = if (jugadorActual == 'X') {
                    'O'
                }else 'X'
            }
        } else {
            Toast.makeText(this, "Has elegido una casilla que está ocupada, vuelve a elegir", Toast.LENGTH_SHORT).show()
        }
    }
    fun partidaGanada(tablero: Map<String, ImageView>): Boolean {
        // Verificar filas
        if (
            (tablero["00"]?.drawable != null &&
                    tablero["00"]?.drawable == tablero["01"]?.drawable &&
                    tablero["01"]?.drawable == tablero["02"]?.drawable) ||
            (tablero["10"]?.drawable != null &&
                    tablero["10"]?.drawable == tablero["11"]?.drawable &&
                    tablero["11"]?.drawable == tablero["12"]?.drawable) ||
            (tablero["20"]?.drawable != null &&
                    tablero["20"]?.drawable == tablero["21"]?.drawable &&
                    tablero["21"]?.drawable == tablero["22"]?.drawable)
        ) {
            return true
        }

        // Verificar columnas
        if (
            (tablero["00"]?.drawable != null &&
                    tablero["00"]?.drawable == tablero["10"]?.drawable &&
                    tablero["10"]?.drawable == tablero["20"]?.drawable) ||
            (tablero["01"]?.drawable != null &&
                    tablero["01"]?.drawable == tablero["11"]?.drawable &&
                    tablero["11"]?.drawable == tablero["21"]?.drawable) ||
            (tablero["02"]?.drawable != null &&
                    tablero["02"]?.drawable == tablero["12"]?.drawable &&
                    tablero["12"]?.drawable == tablero["22"]?.drawable)
        ) {
            return true
        }

        // Verificar diagonales
        if (
            (tablero["00"]?.drawable != null &&
                    tablero["00"]?.drawable == tablero["11"]?.drawable &&
                    tablero["11"]?.drawable == tablero["22"]?.drawable) ||
            (tablero["02"]?.drawable != null &&
                    tablero["02"]?.drawable == tablero["11"]?.drawable &&
                    tablero["11"]?.drawable == tablero["20"]?.drawable)
        ) {
            return true
        }

        return false
    }
    fun reiniciarTablero(tablero: Map<String, ImageView>) {
        // Restablece todas las casillas del tablero a la imagen inicial "R.drawable.pregunta"
        for ((_, imageView) in tablero) {
            imageView.setImageResource(R.drawable.pregunta)
        }
        jugadorActual='X'
    }
    fun reiniciarJuego(tablero: Map<String, ImageView>) {
        // Restablece todas las casillas del tablero a la imagen inicial "R.drawable.pregunta"
        for ((_, imageView) in tablero) {
            imageView.setImageResource(R.drawable.pregunta)
        }
        jugadorActual='X'
        binding.ganJug1.setText(0) // Reinicia el marcador de victorias del Jugador 1
        binding.ganJug2.setText(0) // Reinicia el marcador de victorias del Jugador 2

    }
    private fun mostrarGanador() {
        Toast.makeText(this, "**** HA GANADO EL JUGADOR $jugadorActual ****", Toast.LENGTH_SHORT).show()
    }



}
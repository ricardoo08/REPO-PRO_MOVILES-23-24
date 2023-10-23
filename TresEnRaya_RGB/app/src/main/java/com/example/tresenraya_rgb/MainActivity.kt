package com.example.tresenraya_rgb

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tresenraya_rgb.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private val TAGJ1 = "CRISTIANO"
    private val TAGJ2 = "ELMUNDO"
    var jugadorActual='X'
    var cont=0
    var cont1=0
    lateinit var tablero: Array<Array<ImageView>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tablero = Array(3) { row ->
            Array(3) { col ->
                val id = resources.getIdentifier("img${row + 1}${col + 1}", "id", packageName)
                findViewById<ImageView>(id)
            }
        }
        for (row in tablero.indices) {
            for (col in tablero[row].indices) {
                tablero[row][col].setOnClickListener {
                    manejarClicCasilla(tablero[row][col], row, col)
                }
                tablero[row][col].tag=(row + col).toString()
            }
            tablero[row][2].tag=(row + 3).toString()
        }
        binding.btnRnc.setOnClickListener {
            reiniciarJuego()
        }
    }
    private fun manejarClicCasilla(imageView: ImageView, fila: Int, columna: Int) {
        if (imageView.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.pregunta)?.constantState) {
            // Verifica si la casilla está vacía

            if (jugadorActual == 'X') {
                imageView.setImageResource(R.drawable.milan_)
                imageView.setTag(TAGJ1)
            } else {
                imageView.setImageResource(R.drawable.inter_milan)
                imageView.setTag(TAGJ2)
                // Cambia la imagen del jugador actual
            }
            if (partidaGanada()) {
                // Verifica si alguien ha ganado
                mostrarGanador()
                if (jugadorActual == 'X') {
                    cont++
                    binding.ganJug1.text = cont.toString()
                } else {
                    cont1++
                    binding.ganJug2.text = cont1.toString()
                }
                reiniciarTablero()
            } else {
                jugadorActual = if (jugadorActual == 'X') {
                    'O'
                } else 'X'
            }

        } else {
            Toast.makeText(this, "Has elegido una casilla que está ocupada, vuelve a elegir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun partidaGanada(): Boolean {
        // Verificar filas
        for (fila in 0..2) {
            if (
                tablero[fila][0].tag == tablero[fila][1].tag &&
                tablero[fila][1].tag == tablero[fila][2].tag &&
                tablero[fila][2].tag == tablero[fila][1].tag
            ) {
                return true
            }
        }

        // Verificar columnas
        for (columna in 0..2) {
            if (
                tablero[0][columna].tag == tablero[1][columna].tag &&
                tablero[1][columna].tag == tablero[2][columna].tag
            ) {
                return true
            }
        }

        // Verificar diagonales
        if (
            (tablero[0][0].tag == tablero[1][1].tag &&
                    tablero[1][1].tag == tablero[2][2].tag) ||
            (tablero[0][2].tag == tablero[1][1].tag &&
                    tablero[1][1].tag == tablero[2][0].tag)
        ) {
            return true
        }

        return false
    }

    private fun reiniciarTablero() {
        // Restablece todas las casillas del tablero a la imagen inicial "R.drawable.pregunta"
        for (fila in tablero) {
            for (imageView in fila) {
                imageView.setImageResource(R.drawable.pregunta)
            }
        }
        for (row in tablero.indices) {
            for (col in tablero[row].indices) {
                tablero[row][col].tag=(row + col).toString()
            }
            tablero[row][2].tag=(row + 3).toString()
        }
        jugadorActual = 'X'
    }

    private fun reiniciarJuego() {
        // Restablece todas las casillas del tablero a la imagen inicial "R.drawable.pregunta"
        for (fila in tablero) {
            for (imageView in fila) {
                imageView.setImageResource(R.drawable.pregunta)
            }
        }
        jugadorActual = 'X'
        binding.ganJug1.text = "0" // Reinicia el marcador de victorias del Jugador 1
        binding.ganJug2.text = "0" // Reinicia el marcador de victorias del Jugador 2
        cont = 0
        cont1 = 0
        for (row in tablero.indices) {
            for (col in tablero[row].indices) {
                tablero[row][col].tag=(row + col).toString()
            }
            tablero[row][2].tag=(row + 3).toString()
        }
    }

    private fun mostrarGanador() {
        Toast.makeText(this, "**** HA GANADO EL JUGADOR $jugadorActual ****", Toast.LENGTH_SHORT).show()
    }
}
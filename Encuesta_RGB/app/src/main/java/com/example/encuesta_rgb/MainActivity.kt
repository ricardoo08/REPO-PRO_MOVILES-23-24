package com.example.encuesta_rgb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.example.encuesta_rgb.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var nombre :String
    lateinit var sisOp : String
    lateinit var espe : String
    var horasEst : Int = 0
    val encuestasList = ArrayList<Encuesta>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (binding.swAnonimo.isChecked) {
            nombre = "Anónimo"
            binding.txtNom.setText("Anónimo")
        } else {
            nombre = binding.txtNom.text.toString()
        }

        binding.gruSis.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.cirLin.id -> {
                    sisOp = "LINUX"
                }

                binding.cirMac.id -> {
                    sisOp = "MAC"
                }

                binding.cirWin.id -> {
                    sisOp = "WINDOWS"
                }
            }
        }
        binding.gruEsp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.cirDaw.id -> {
                    espe = "DAW"
                }

                binding.cirDam.id -> {
                    espe = "DAM"
                }

                binding.cirAsi.id -> {
                    espe = "ASIR"
                }
            }
        }
        horasEst= binding.seekBar.progress
        binding.txtBar.setText(horasEst.toString())
    }
    fun Validar(view: View) {
        val encuesta = Encuesta(nombre, sisOp, espe, horasEst)
        encuestasList.add(encuesta)
        nombre = "Anónimo"
        sisOp = ""
        espe = ""
        horasEst = 0
        binding.cajaNombres.text = ""
    }
    fun Cuantas(view: View) {
        val totalEncuestas = encuestasList.size
        val mensaje = "Total de encuestas realizadas: $totalEncuestas"
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
    fun Resumen(view: View) {
        val resumenEncuestas = StringBuilder()

        for (encuesta in encuestasList) {
            resumenEncuestas.append(encuesta.toString())
            resumenEncuestas.append("\n\n")
        }
        binding.cajaNombres.text = resumenEncuestas.toString()
    }
    fun Reiniciar(view: View) {
        encuestasList.clear()
        nombre = ""
        sisOp = ""
        espe = ""
        horasEst = 0
        binding.cajaNombres.text = ""
        binding.txtBar.text = ""
        binding.txtNom.text.clear()
        binding.swAnonimo.isChecked = false
        binding.seekBar.progress = 0
        binding.gruSis.clearCheck()
        binding.gruEsp.clearCheck()
    }
}
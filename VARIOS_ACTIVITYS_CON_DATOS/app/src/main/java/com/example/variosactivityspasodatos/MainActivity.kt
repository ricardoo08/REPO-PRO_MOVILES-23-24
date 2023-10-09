package com.example.variosactivityspasodatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.variosactivityspasodatos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boton.setOnClickListener {
            irAVentana2()
        }
    }

    private fun irAVentana2() {
        //podriamos crear aqui el objeto y pasarlo,en vez de los atributos individualmente.
        // Persona deberia ser serializable en ese caso
        var miIntent: Intent = Intent(this, Ventana_2::class.java)
        //miIntent.putExtra("nombre",binding.cajaNombre.text.toString())
        //miIntent.putExtra("edad",binding.cajaEdad.text.toString())
        startActivity(miIntent)
    }
}
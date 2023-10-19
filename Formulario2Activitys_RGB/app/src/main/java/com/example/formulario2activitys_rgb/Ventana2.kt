package com.example.formulario2activitys_rgb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.formulario2activitys_rgb.databinding.ActivityMainBinding
import com.example.formulario2activitys_rgb.databinding.ActivityVentana2Binding

class Ventana2 : AppCompatActivity() {
    lateinit var binding: ActivityVentana2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentana2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val dni = intent.getStringExtra("dni")
        val gmail = intent.getStringExtra("gmail")
        val contrasena = intent.getStringExtra("contrasena")
        binding.nom.setText(nombre)
        binding.ape.setText(apellido)
        binding.dni.setText(dni)
        binding.gmail.setText(gmail)
        binding.contrasena.setText(contrasena)
        var cadena :String =""
        var i:Int=1
        for (p in Singleton.listaUsuarios) {
            cadena += "$i. Nombre: ${p.nombre}  Apellido: ${p.apellido}  DNI: ${p.dni}  Gmail: ${p.gmail}  Contraseña: ${p.contrasena}\n"
            i++

        }
        binding.multiLine.setText(cadena)
        binding.btnVol.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
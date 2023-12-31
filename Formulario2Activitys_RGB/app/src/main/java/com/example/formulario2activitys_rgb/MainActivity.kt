package com.example.formulario2activitys_rgb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.formulario2activitys_rgb.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReg.setOnClickListener {
            if (validarCampos()) {
                val nombre = binding.txtNom.text.toString()
                val apellido = binding.txtApe.text.toString()
                val dni = binding.txtDNI.text.toString()
                val gmail = binding.txtGmail.text.toString()
                val contrasena = binding.txtCon.text.toString()

                if (usuarioExiste(dni)) {
                    Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    if (contrasena.length < 6) {
                        Toast.makeText(
                            this,
                            "La contraseña debe tener al menos 6 caracteres",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val nuevoUsuario = Persona(nombre, apellido, dni, gmail, contrasena)
                        Singleton.listaUsuarios.add(nuevoUsuario)

                        irVentana2()

                    }
                }
            }
        }
    }

    private fun validarCampos(): Boolean {
        val nombre = binding.txtNom.text.toString()
        val apellido = binding.txtApe.text.toString()
        val dni = binding.txtDNI.text.toString()
        val gmail = binding.txtGmail.text.toString()
        val contrasena = binding.txtCon.text.toString()

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) ||
            TextUtils.isEmpty(dni) || TextUtils.isEmpty(gmail) ||
            TextUtils.isEmpty(contrasena)
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun usuarioExiste(dni: String): Boolean {
        for (usuario in Singleton.listaUsuarios) {
            if (usuario.dni == dni) {
                return true
            }
        }
        return false
    }
    private fun irVentana2(){
        val intent = Intent(this, Ventana2::class.java)
        intent.putExtra("nombre", binding.txtNom.text.toString())
        intent.putExtra("apellido", binding.txtApe.text.toString())
        intent.putExtra("dni", binding.txtDNI.text.toString())
        intent.putExtra("gmail", binding.txtGmail.text.toString())
        intent.putExtra("contrasena",binding.txtCon.text.toString())
        startActivity(intent)
    }
}

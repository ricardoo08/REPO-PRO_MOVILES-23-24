package com.example.recycleviewconsqlite

import Adaptadores.MiAdaptadorRecycler
import Auxiliar.Conexion
import Modelo.Almacen
import Modelo.Persona
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewconsqlite.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var miRecyclerView : RecyclerView
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextoPrincipal: Context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var pers:Persona
        binding.btnAdd.setOnClickListener {
            if (binding.edNom.text.toString().trim().isEmpty() || binding.edNac.text.toString().trim().isEmpty()
                || binding.edEqu.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
            }
            else {
                pers =  Persona(
                    binding.edNom.getText().toString(),
                    binding.edNac.getText().toString(),
                    binding.edEqu.getText().toString()
                )
                var codigo= Conexion.addPersona(this, pers)
                binding.edNom.setText("")
                binding.edNac.setText("")
                binding.edEqu.setText("")
                binding.edNom.requestFocus()
                //la L es por ser un Long lo que trae codigo.
                if(codigo!=-1L) {
                    Toast.makeText(this, "Persona insertada", Toast.LENGTH_SHORT).show()
                    //listarPersonas()
                }
                else
                    Toast.makeText(this, "Ya existe ese DNI. Persona NO insertada", Toast.LENGTH_SHORT).show()
            }
        }
    }
    @JvmOverload
    fun listarPersonas(view: View) {
        var lista = Conexion.obtenerPersonas(this)
        if (lista.size == 0) {
            Toast.makeText(this, "No existen datos en la tabla", Toast.LENGTH_SHORT).show()
        } else {
            miRecyclerView = binding.listaPersonasRecycler as RecyclerView
            miRecyclerView.setHasFixedSize(true)
            miRecyclerView.layoutManager = LinearLayoutManager(this)
            // Inicializa el adaptador con los datos que deseas mostrar // Reemplaza con la función que obtiene tus datos

            var adapter = MiAdaptadorRecycler(lista, this) // Reemplaza con tu adaptador personalizado

            miRecyclerView.adapter = adapter // Asigna el adaptador al RecyclerView
        }
    }

    annotation class JvmOverload
}
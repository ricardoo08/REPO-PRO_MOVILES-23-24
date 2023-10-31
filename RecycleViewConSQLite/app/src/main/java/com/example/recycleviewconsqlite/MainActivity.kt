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
        binding.btnAdd.setOnClickListener {
            if (binding.edNom.text.toString().trim().isEmpty() || binding.edNac.text.toString().trim().isEmpty()
                || binding.edEqu.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
            }
            else {
                var pers: Persona = Persona(
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
                    listarPersonas(miRecyclerView)
                }
                else
                    Toast.makeText(this, "Ya existe ese DNI. Persona NO insertada", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun listarPersonas(view: View) {
        var listado:ArrayList<Persona> = Conexion.obtenerPersonas(this)
        if (listado.size==0) {
            Toast.makeText(this, "No existen datos en la tabla", Toast.LENGTH_SHORT).show()
        }
        else {
            miRecyclerView = binding.listaPersonasRecycler as RecyclerView
            miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
            miRecyclerView.layoutManager = LinearLayoutManager(this)//se dice el tipo de Layout, dejampos este.
            //esta es la clave. Creo un objeto de tipo Mi Adaptador y le paso la lista que he creado prevaimente más arriba.
            //aquí, es donde inflará y pintará cada CardView.
            var miAdapter = MiAdaptadorRecycler(Almacen.personas, this)
            //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
            miRecyclerView.adapter = miAdapter
            contextoPrincipal = this
        }
    }
}
package com.example.listas_rgb

import Adaptadores.MiAdaptadorRecycler
import Adaptadores.MiAdaptadorRecycler2
import Auxiliar.Conexion
import Auxiliar.Conexion2
import Modelo.Lista
import Modelo.Nota
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listas_rgb.databinding.ActivityHomeLogBinding
import com.example.listas_rgb.databinding.ActivityNotasBinding
import com.google.firebase.firestore.FirebaseFirestore

class Notas() : AppCompatActivity() {
    lateinit var binding: ActivityNotasBinding
    lateinit var miRecyclerView2 : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)
        binding = ActivityNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var listaNotas = Conexion2.obtenerNotas(this)
        miRecyclerView2 = binding.recyclerView2 as RecyclerView
        miRecyclerView2.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView2.layoutManager = LinearLayoutManager(this)//se dice el tipo de Layout, dejampos este.
        //esta es la clave. Creo un objeto de tipo Mi Adaptador y le paso la lista que he creado prevaimente más arriba.
        //aquí, es donde inflará y pintará cada CardView.
        var miAdapter = MiAdaptadorRecycler2(listaNotas, this)
        //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
        miRecyclerView2.adapter = miAdapter
        binding.btnAdd.setOnClickListener {
            //Se guardarán en modo HashMap (clave, valor).

            if (binding.edProducto.text.toString().trim()
                    .isEmpty() || binding.edCantidad.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
            } else {
                var not: Nota = Nota(
                    binding.edProducto.text.toString(),
                    binding.edCantidad.text.toString().toInt()
                )
                var codigo = Conexion2.addNota(this, not)
                binding.edProducto.setText("")
                binding.edCantidad.setText("")

                binding.edProducto.requestFocus()
                //la L es por ser un Long lo que trae codigo.
                if (codigo != -1L) {
                    Toast.makeText(this, "Nota insertada", Toast.LENGTH_SHORT).show()
                    listaNotas = Conexion2.obtenerNotas(this)
                    var miAdapter = MiAdaptadorRecycler2(listaNotas, this)
                    //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
                    miRecyclerView2.adapter = miAdapter
                } else
                    Toast.makeText(
                        this,
                        "Ya existe ese nombre. Nota NO insertada",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        binding.btnBorrar.setOnClickListener {
            var listaNotas = Conexion2.obtenerNotas(this)
            if (MiAdaptadorRecycler2.seleccionado >= 0) {
                val no = listaNotas.get(MiAdaptadorRecycler2.seleccionado)
                Conexion2.delNota(this,no.producto)
                Toast.makeText(this, "Nota eliminada: ${no.producto}", Toast.LENGTH_SHORT).show()
                // También puedes actualizar la lista en tu interfaz de usuario o recargarl
                listaNotas.clear()
                listaNotas = Conexion2.obtenerNotas(this)
                var miAdapter = MiAdaptadorRecycler2(listaNotas, this)
                //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
                miRecyclerView2.adapter = miAdapter
            }


        }

        binding.btnListar.setOnClickListener {
            var listaNotas = Conexion2.obtenerNotas(this)
            //esta es la clave. Creo un objeto de tipo Mi Adaptador y le paso la lista que he creado prevaimente más arriba.
            //aquí, es donde inflará y pintará cada CardView.
            var miAdapter = MiAdaptadorRecycler2(listaNotas, this)
            //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
            miRecyclerView2.adapter = miAdapter

        }
        binding.btVolver2.setOnClickListener {
            // Manejar el clic en el botón de volver
            finish() // Cierra la actividad actual y vuelve a la actividad anterior en el historial
        }
        binding.btnEditar.setOnClickListener {
            eliminarTodasListas()
        }
    }
    fun eliminarTodasListas() {
        // Eliminar de la interfaz de usuario
        var listaNotas: ArrayList<Nota> = Conexion2.obtenerNotas(this)
        val cantidadEliminada = Conexion2.delTodasNotas(this)
        listaNotas.clear()
        listaNotas = Conexion2.obtenerNotas(this)
        var miAdapter = MiAdaptadorRecycler2(listaNotas, this)
        //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
        miRecyclerView2.adapter = miAdapter

        }
}
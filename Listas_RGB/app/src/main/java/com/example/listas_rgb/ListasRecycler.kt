package com.example.listas_rgb

import Adaptadores.MiAdaptadorRecycler
import Auxiliar.Conexion
import Modelo.Lista
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listas_rgb.databinding.ActivityListasRecyclerBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListasRecycler : AppCompatActivity() {
    lateinit var binding: ActivityListasRecyclerBinding

    lateinit var miRecyclerView : RecyclerView
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextoPrincipal: Context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListasRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listaPersonas: ArrayList<Lista> = intent.getParcelableArrayListExtra("lista_personas")!!
        miRecyclerView = binding.recyclerView as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(this)//se dice el tipo de Layout, dejampos este.
        //esta es la clave. Creo un objeto de tipo Mi Adaptador y le paso la lista que he creado prevaimente más arriba.
        //aquí, es donde inflará y pintará cada CardView.
        var miAdapter = MiAdaptadorRecycler(listaPersonas, this)
        //aquí es donde hace la "magia", al pasarle a mi Recicler View, el adaptador creado.
        miRecyclerView.adapter = miAdapter
        contextoPrincipal = this
        binding.btVolver1.setOnClickListener {
            // Manejar el clic en el botón de volver
            finish() // Cierra la actividad actual y vuelve a la actividad anterior en el historial
        }
        binding.btEliminarLista.setOnClickListener {
            if (MiAdaptadorRecycler.seleccionado >= 0) {
                val li = listaPersonas.get(MiAdaptadorRecycler.seleccionado)
                val db = FirebaseFirestore.getInstance()
                Conexion.delLista(this,li.nombre)
                Toast.makeText(this, "Lista eliminada: ${li.nombre}", Toast.LENGTH_SHORT).show()
                // También puedes actualizar la lista en tu interfaz de usuario o recargarla
                db.collection("listas")
                    .document(li.nombre)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Lista eliminada: ${li.nombre}", Toast.LENGTH_SHORT).show()
                        val nuevaLista = Conexion.obtenerListas(this)
                        miAdapter.actualizarLista(nuevaLista)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al intentar eliminar la lista: ${li.nombre}", Toast.LENGTH_SHORT).show()
                    }
            }

        }
        binding.btDetalle.setOnClickListener {
            if (MiAdaptadorRecycler.seleccionado >= 0) {
                val pe = listaPersonas.get(MiAdaptadorRecycler.seleccionado)
                Log.e("ACSCO",pe.toString())
                var inte : Intent = Intent(this, Notas::class.java)
                inte.putExtra("obj",listaPersonas.get(MiAdaptadorRecycler.seleccionado))
                ContextCompat.startActivity(HomeLog.contextoPrincipal, inte, null)
            }
            else {
                Toast.makeText(this,"Selecciona algo previamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
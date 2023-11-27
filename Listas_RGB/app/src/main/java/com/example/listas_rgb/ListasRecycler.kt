package com.example.listas_rgb

import Adaptadores.MiAdaptadorRecycler
import Modelo.Lista
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listas_rgb.databinding.ActivityListasRecyclerBinding

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
        val listaPersonas: ArrayList<Lista>? = intent.getParcelableArrayListExtra("lista_personas")

        if (listaPersonas != null) {
            // La lista está presente, puedes continuar con su uso
            miRecyclerView = binding.recyclerView
            miRecyclerView.setHasFixedSize(true)
            miRecyclerView.layoutManager = LinearLayoutManager(this)
            val miAdapter = MiAdaptadorRecycler(listaPersonas, this)
            miRecyclerView.adapter = miAdapter
            contextoPrincipal = this
        } else {
            // La lista no está presente, manejar el caso de error o proporcionar un valor predeterminado
            // Puedes mostrar un mensaje de error y finalizar la actividad
            Toast.makeText(this, "Error: No se encontró la lista de personas", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
package com.example.listas_rgb

import Adaptadores.MiAdaptadorRecycler
import Modelo.Lista
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        setContentView(R.layout.activity_listas_recycler)
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
    }
}
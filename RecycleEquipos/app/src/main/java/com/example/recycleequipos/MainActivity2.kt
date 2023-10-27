package com.example.recycleequipos

import Modelo.Persona
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recycleequipos.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var p  = intent.getSerializableExtra("obj") as Persona
        binding.ponEqu.text = p.nombre
        binding.ponNac.text = p.nacionalidad
        val imageName = p.imagen // Nombre de la imagen en forma de String (por ejemplo, "mi_imagen")
        if (imageName == "barsa"){
            binding.imageView.setImageResource(R.drawable.barsa)
        }
        if (imageName == "inter_milan"){
            binding.imageView.setImageResource(R.drawable.inter_milan)
        }
        if (imageName == "milan_"){
            binding.imageView.setImageResource(R.drawable.milan_)
        }
        if (imageName == "madrid"){
            binding.imageView.setImageResource(R.drawable.madrid)
        }

        binding.btnVol.setOnClickListener {
            finish()
        }

    }
}
package com.example.recyclerview

import Modelo.Personaje
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var p  = intent.getSerializableExtra("obj") as Personaje
        binding.txtPersona2.text = p.toString()

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
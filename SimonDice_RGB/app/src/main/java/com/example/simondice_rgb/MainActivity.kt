package com.example.simondice_rgb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simondice_rgb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var random = (0..3).random()
        val colores = arrayOf("Verde","Amarillo","Azul","Rojo")
        val listColor: ArrayList<String> = arrayListOf(colores[random])
        val activitiesArray = arrayOf(Green::class.java)
        for (i in 0..3){
            random = (0..3).random()
            listColor.add(colores[random])
        }
        binding.btnEmp.setOnClickListener {
            val intent = Intent(this,Green::class.java)
            intent.putStringArrayListExtra("colors",listColor)
            intent.putExtra("cont",0)
            intent.putExtra("punt",0)
            startActivity(intent)
        }
    }
}
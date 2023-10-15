package com.example.encuesta_rgb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.encuesta_rgb.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var nombre :String
    lateinit var sisOp : String
    lateinit var espe : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (binding.swAnonimo.isChecked) {
            nombre = "Anónimo"
        } else {
            nombre = binding.cajaNombres.text.toString()
        }
        binding.gruSis.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.cirLin.id -> {
                    sisOp = "LINUX"
                }

                binding.cirMac.id -> {
                    sisOp = "MAC"
                }

                binding.cirWin.id -> {
                    sisOp = "WINDOWS"
                }
            }
        }
        binding.gruEsp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.cirDaw.id -> {
                    espe = "DAW"
                }

                binding.cirDam.id -> {
                    espe = "DAM"
                }

                binding.cirAsi.id -> {
                    espe = "ASIR"
                }
            }
        }
    }
}
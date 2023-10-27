package com.example.simondice_rgb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.simondice_rgb.databinding.ActivityBlueBinding
import com.example.simondice_rgb.databinding.ActivityMainBinding

class Blue : AppCompatActivity() {
    lateinit var binding: ActivityBlueBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var punt = intent.getIntExtra("punt",-2)
        var cont = intent.getIntExtra("cont",-3)
        val colors = intent.getStringArrayListExtra("colors")
        val activityArray = arrayOf(Green::class.java, Yellow::class.java,Blue::class.java,Red::class.java)
        binding.txtPunt.text = punt.toString()
        if (punt != cont){
            val temp = "Color: "+(cont+1)
            binding.titleText.text = temp
        }else{
            val temp = "Simon says "+ colors!![cont]
            binding.titleText.text = temp
        }
        fun restart(tit:String){
            colors!![cont]=tit
            binding.titleText.text = tit
            binding.btnRoj.text = tit
            binding.btnAma.text = tit
            binding.btnVer.text = tit
            binding.btnMor.visibility = View.VISIBLE
        }
        fun onCorrect(color:String,classNum:Int) {
            if (colors!![cont] == color){
                val intent = Intent(this, activityArray[classNum])
                if ((cont + 1) == colors!!.size) {
                    restart("HAS GANADO")
                } else {
                    if (cont == punt) {
                        cont--
                        punt++
                    }
                    cont++
                    intent.putStringArrayListExtra("colors",colors)
                    intent.putExtra("cont", cont)
                    intent.putExtra("punt", punt)
                    startActivity(intent)

                }
            }else if (binding.btnMor.visibility !=0){
                restart("HAS PERDIDO")
            }

        }


        binding.btnVer.setOnClickListener {
            onCorrect("Verde",0)
        }
        binding.btnAma.setOnClickListener {
            onCorrect("Amarillo",1)
        }
        binding.btnAzu.setOnClickListener {
            onCorrect("Azul",2)
        }
        binding.btnRoj.setOnClickListener {
            onCorrect("Rojo",3)
        }
        binding.btnMor.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }


    }

}
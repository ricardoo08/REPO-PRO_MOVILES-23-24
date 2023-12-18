package com.example.fragmentsrgb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fragmentsrgb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btF1.setOnClickListener {
            val mFragmentManager = supportFragmentManager
            val fragmentTransaction = mFragmentManager.beginTransaction()
            val fragment1 = fragment1()

            val mBundle = Bundle()
            mBundle.putString("edTextActivity",binding.edCaja.text.toString())
            fragment1.arguments = mBundle
            fragmentTransaction.replace(R.id.miFragmento, fragment1).commit()
        }

        binding.btF2.setOnClickListener {
            val fragmentoB = fragment2()

            val fragmentTransaction =supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.miFragmento, fragmentoB)
            fragmentTransaction.commit()
        }
    }
}
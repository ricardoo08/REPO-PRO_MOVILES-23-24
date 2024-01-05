package com.example.listas_rgb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.listas_rgb.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    lateinit var navegation : BottomNavigationView
    private val NavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.itemFragmentLista -> {
                supportFragmentManager.commit {

                    replace<FragmentLista>(binding.contenedorFragment.id)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.itemFragmentHuevo -> {
                supportFragmentManager.commit {

                    replace<FragmentHuevo>(binding.contenedorFragment.id)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener (NavMenu)
        supportFragmentManager.commit {

            replace<FragmentLista>(binding.contenedorFragment.id)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }
    }
}
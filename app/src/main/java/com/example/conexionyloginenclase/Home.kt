package com.example.conexionyloginenclase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.conexionyloginenclase.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    var miArray:ArrayList<User> = ArrayList()  //Este será el arrayList que se usará para el adapter del RecyclerView o de la ListView.
    //Valores fake.
    val nombres = listOf("Ragnar","Ivar","Lagertha","Floki")
    val apellidos = listOf("Lothbrok","Sin huesos","Piel de Hierro","Semi diosa")
    val edades = listOf(18, 23, 45, 67, 34, 47, 41)

    lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseauth : FirebaseAuth
    val TAG = "ACSCO"
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
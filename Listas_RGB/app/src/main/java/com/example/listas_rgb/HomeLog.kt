package com.example.listas_rgb

import Auxiliar.Conexion
import Modelo.Lista
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.firebase.User
import com.example.listas_rgb.databinding.ActivityHomeLogBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeLog : AppCompatActivity() {
    var miLista:ArrayList<Lista> = ArrayList()  //Este será el arrayList que se usará para el adapter del RecyclerView o de la ListView.
    lateinit var binding: ActivityHomeLogBinding
    private lateinit var firebaseauth : FirebaseAuth
    val TAG = "ACSCO"
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
        binding = ActivityHomeLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para la autenticación, de cualquier tipo.
        firebaseauth = FirebaseAuth.getInstance()

        //Recuperamos los datos del login.
        binding.txtEmail.text = intent.getStringExtra("email").toString()
        binding.txtProveedor.text = intent.getStringExtra("provider").toString()

        binding.btCerrarSesion.setOnClickListener {
            Log.e(TAG, firebaseauth.currentUser.toString())
            // Olvidar al usuario, limpiando cualquier referencia persistente
            //comprobadlo en Firebase, como ha desaparecido.
//            firebaseauth.currentUser?.delete()?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    firebaseauth.signOut()
//                    Log.e(TAG,"Cerrada sesión completamente")
//                } else {
//                    Log.e(TAG,"Hubo algún error al cerrar la sesión")
//                }
//            }
            firebaseauth.signOut()

            val signInClient = Identity.getSignInClient(this)
            signInClient.signOut()
            Log.e(TAG, "Cerrada sesión completamente")
            finish()

        }
        binding.btVolver.setOnClickListener {
            // Log.e(TAG, firebaseauth.currentUser.toString())
            firebaseauth.signOut()
            finish()
        }

        binding.btGuardar.setOnClickListener {
            //Se guardarán en modo HashMap (clave, valor).

            if (binding.edNombre.text.toString().trim().isEmpty() || binding.edDia.text.toString().trim().isEmpty()
                || binding.edMes.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
            }
            else {
                var list: Lista = Lista(
                    binding.edNombre.getText().toString(),
                    binding.edMes.getText().toString().toInt(),
                    binding.edDia.getText().toString().toInt()
                )
                var codigo= Conexion.addLista(this, list)
                binding.edNombre.setText("")
                binding.edMes.setText("")
                binding.edDia.setText("")
                binding.edNombre.requestFocus()
                //la L es por ser un Long lo que trae codigo.
                if(codigo!=-1L) {
                    Toast.makeText(this, "Persona insertada", Toast.LENGTH_SHORT).show()
                    db.collection("listas")
                        .document(list.nombre.toString()) //Será la clave del documento.
                        .set(list).addOnSuccessListener {
                            Toast.makeText(this, "Almacenado", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                        }
                }
                else
                    Toast.makeText(this, "Ya existe ese DNI. Persona NO insertada", Toast.LENGTH_SHORT).show()
            }

            // Si no existe el documento lo crea, si existe lo remplaza.

        }
        binding.btEliminarLista.setOnClickListener {
            if (binding.edNombre.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Ingrese un nombre para eliminar", Toast.LENGTH_SHORT).show()
            } else {
                val nombre = binding.edNombre.text.toString()
                val exito = Conexion.delLista(this, nombre)

                if (exito > 0) {
                    Toast.makeText(this, "Lista eliminada: $nombre", Toast.LENGTH_SHORT).show()
                    // También puedes actualizar la lista en tu interfaz de usuario o recargarla
                    db.collection("listas")
                        .document(nombre)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Lista eliminada: $nombre", Toast.LENGTH_SHORT).show()
                            // También puedes actualizar la lista en tu interfaz de usuario o recargarla
                            miLista = listarPersonas()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al intentar eliminar la lista: $nombre", Toast.LENGTH_SHORT).show()
                        }
                } else if (exito == 0) {
                    Toast.makeText(this, "No se encontró la lista: $nombre", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al intentar eliminar la lista: $nombre", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    fun listarPersonas(): ArrayList<Lista> {
        var listado:ArrayList<Lista> = Conexion.obtenerListas(this)
        return listado
    }
}
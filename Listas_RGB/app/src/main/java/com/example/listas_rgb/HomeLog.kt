package com.example.listas_rgb

import Adaptadores.MiAdaptadorRecycler
import Auxiliar.Conexion
import Modelo.Lista
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.firebase.User
import com.example.listas_rgb.databinding.ActivityHomeLogBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeLog : AppCompatActivity() {
    var miLista:ArrayList<Lista> = ArrayList()  //Este será el arrayList que se usará para el adapter del RecyclerView o de la ListView.
    lateinit var binding: ActivityHomeLogBinding
    private lateinit var firebaseauth : FirebaseAuth
    val TAG = "ACSCO"
    val db = Firebase.firestore
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextoPrincipal: Context
    }
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
        val email = intent.getStringExtra("email").toString()
        binding.btEliminar.setOnClickListener {
            //Buscamos antes si existe un campo con ese email en un documento.
            val id = db.collection("users")
                .whereEqualTo("email",binding.txtEmail.text.toString())
                .get()
                .addOnSuccessListener {result ->
                    //En result, vienen los que cumplen la condición (si no pongo nada es it)
                    //Con esto borramos el primero.
                    //db.collection("users").document(result.elementAt(0).id).delete().toString()
                    //Con esto borramos todos. No olvidar que id aquí no es una Primarykey, puede repetirse.
                    for (document in result) {
                        db.collection("users")
                            .document(document.id)
                            .delete().toString() //lo importante aquí es el delete. el toString es pq además devuelve un mensaje con lo sucedido.
                    }

                    Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "No se ha encontrado el documento a eliminar", Toast.LENGTH_SHORT).show()
                }


        }
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
                    Toast.makeText(this, "Lista insertada", Toast.LENGTH_SHORT).show()
                    db.collection("listas")
                        .document(list.nombre.toString()) //Será la clave del documento.
                        .set(list).addOnSuccessListener {
                            Toast.makeText(this, "Almacenado", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                        }
                }
                else
                    Toast.makeText(this, "Ya existe ese nombre. Persona NO insertada", Toast.LENGTH_SHORT).show()
            }

            // Si no existe el documento lo crea, si existe lo remplaza.

        }

        binding.btMostar.setOnClickListener {
            // Obtener la lista de personas usando la función listarPersonas
            val listadoPersonas: ArrayList<Lista> = listarPersonas()

            // Crear un Intent para iniciar la actividad ListasRecycler
            val intent = Intent(this, ListasRecycler::class.java)

            // Pasar la lista como un extra al Intent
            intent.putExtra("lista_personas", listadoPersonas)

            // Iniciar la nueva actividad
            startActivity(intent)

        }

        binding.btEliminarListas.setOnClickListener {
            eliminarTodasListas()
        }
        contextoPrincipal = this
    }
    fun eliminarTodasListas() {
        // Eliminar de la interfaz de usuario
        val listaPersonas: ArrayList<Lista>? = listarPersonas()


        // Eliminar de la base de datos Firestore
        val db = FirebaseFirestore.getInstance()
        val listasCollection = db.collection("listas")

        listasCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val cantidadEliminada = Conexion.delTodasListas(this)
                    // Eliminar cada documento (lista) en la colección
                    listasCollection.document(document.id).delete()
                }
                Toast.makeText(this, "Todas las listas eliminadas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al intentar eliminar las listas", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun listarPersonas(): ArrayList<Lista> {
        var listado:ArrayList<Lista> = Conexion.obtenerListas(this)
        return listado
    }
}
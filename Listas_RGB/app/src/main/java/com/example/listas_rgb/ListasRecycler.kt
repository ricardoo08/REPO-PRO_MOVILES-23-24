package com.example.listas_rgb

import Modelo.ListaDentro
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.listas_rgb.databinding.ActivityNotasBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListasRecycler : AppCompatActivity() {
    private lateinit var binding: ActivityNotasBinding
    private lateinit var email: String  // Asegúrate de obtener el email de alguna manera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa tu RecyclerView y su adaptador

        // Obtén el email del usuario (de alguna manera, puede ser pasado desde la actividad anterior)
        email = intent.getStringExtra("email").toString()
        binding.intNombre.text = intent.getStringExtra("nombreLista").toString()
        binding.intDia.text = intent.getStringExtra("diaLista")
        binding.btnGuardar.setOnClickListener {
            // Agrega un nuevo elemento a Firebase Firestore (esto es solo un ejemplo)
            agregarElementoAFirestore()
        }
        binding.btnBorrar.setOnClickListener {
            borrarDatosDesdeFirestore()
            binding.txtNot.setText("")
        }
        binding.btnVolver.setOnClickListener {
            onBackPressed()        }
        // Carga los datos desde Firestore al RecyclerView (puedes llamar a esta función según sea necesario)
        cargarDatosDesdeFirestore(email)
    }
    private fun agregarElementoAFirestore() {
        val nuevoItem = ListaDentro(binding.txtNot.text.toString())
        val intNom = intent.getStringExtra("nombreLista").toString()

        // Obtén la referencia al documento específico en la colección "notas"
        val documentoRef =
            FirebaseFirestore.getInstance().collection("users").document(email).collection("listas").document(intNom).collection("notas").document(intNom)

        // Actualiza el documento existente con el nuevoItem
        documentoRef.set(nuevoItem)
            .addOnSuccessListener {
                // Éxito al actualizar el documento
                Log.d(TAG, "Documento actualizado con ID: ${documentoRef.id}")
            }
            .addOnFailureListener { e ->
                // Error al actualizar el documento
                Log.w(TAG, "Error al actualizar documento", e)
            }
    }
    private fun cargarDatosDesdeFirestore(email: String) {
        val db = FirebaseFirestore.getInstance()
        val intNom = intent.getStringExtra("nombreLista").toString()

        db.collection("users").document(email).collection("listas").document(intNom).collection("notas")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val descr = document.getString("descripcion") ?: ""
                    binding.txtNot.setText(descr)
                }

            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener las notas", e)
            }
    }
    private fun borrarDatosDesdeFirestore() {
        val db = FirebaseFirestore.getInstance()
        val intNom = intent.getStringExtra("nombreLista").toString()

        // Elimina el documento de la colección en Firebase
        db.collection("users")
            .document(email)
            .collection("listas")
            .document(intNom).collection("notas").document(intNom)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Lista eliminada",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al eliminar la lista", e)
                Toast.makeText(
                    this,
                    "Error al eliminar la lista",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    companion object {
        private const val TAG = "ListasRecycler"
    }
}
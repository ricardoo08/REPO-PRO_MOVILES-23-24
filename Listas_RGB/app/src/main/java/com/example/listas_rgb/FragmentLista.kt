package com.example.listas_rgb

import Adaptadores.ListaAdapter
import Modelo.Lista
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listas_rgb.databinding.FragmentListaBinding
import com.example.listas_rgb.databinding.ItemCardBinding
import com.example.listas_rgb.databinding.ListDialogBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var cont = 0

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLista.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLista : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentListaBinding
    private lateinit var binding2: ListDialogBinding
    private lateinit var binding3: ItemCardBinding
    private val listLista = ArrayList<Lista>()
    private lateinit var listaAdapter: ListaAdapter
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        binding2 = ListDialogBinding.inflate(inflater, container, false)
        binding3 = ItemCardBinding.inflate(inflater,container,false)
        val view = binding.root  // No necesitas el operador de llamada segura aquí
        val email = requireActivity().intent.getStringExtra("email")

        // Asignar el clic del botón para mostrar el diálogo
        binding.botonAmarilloCircular.setOnClickListener {
            if (email != null) {
                showLoginDialog(email)
            }
        }
        if (email != null) {
            cargarNotasDesdeFirestore(email)
        }

        binding.botonCerrar.setOnClickListener {
            if (email != null) {
                eliminarLista(email)
            }

        }
        binding.botonFlecha.setOnClickListener {
            val li = listLista.get(ListaAdapter.seleccionado)
            val intent = Intent(context, ListasRecycler::class.java)
            // Puedes pasar datos adicionales al nuevo Activity si es necesario
            intent.putExtra("nombreLista", li.nombre)
            intent.putExtra("diaLista", li.dia)
            intent.putExtra("email",email)
            context?.startActivity(intent)
        }
        binding.btnCerrar.setOnClickListener {
            // Cerrar sesión en Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            // Cerrar sesión en Identity Toolkit (si lo estás utilizando)
            val signInClient = Identity.getSignInClient(requireContext())
            signInClient.signOut()

            // Redirigir a la pantalla de inicio de sesión u otra actividad
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()  // Finaliza la actividad actual para evitar que el usuario vuelva atrás
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentLista.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLista().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun eliminarLista(email: String) {
        if (ListaAdapter.seleccionado >= 0) {
            val li = listLista[ListaAdapter.seleccionado]

            val db = FirebaseFirestore.getInstance()

            // Elimina el documento de la colección en Firebase
            db.collection("users").document(email).collection("listas").document(li.nombre)
                .delete()
                .addOnSuccessListener {
                    // Elimina el elemento de la lista local y notifica al adaptador
                    listLista.removeAt(ListaAdapter.seleccionado)
                    listaAdapter.notifyDataSetChanged()

                    Toast.makeText(
                        requireContext(),
                        "Lista eliminada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al eliminar la lista", e)
                    Toast.makeText(
                        requireContext(),
                        "Error al eliminar la lista",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    @SuppressLint("MissingInflatedId")
        private fun showLoginDialog(email: String) {
            val dialogView = layoutInflater.inflate(R.layout.list_dialog, null)
            val edList = dialogView.findViewById<EditText>(R.id.edList)
            val edDia = dialogView.findViewById<EditText>(R.id.edDia)

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Crea tu lista")
                .setView(dialogView)
                .setPositiveButton("Crear lista") { _, _ ->
                    val nombreLista = edList.text.toString()
                    val descripcionLista = edDia.text.toString()

                    if (nombreLista.isNotEmpty() && descripcionLista.isNotEmpty()) {
                        val listaMap = hashMapOf(
                            "nombre" to nombreLista,
                            "dia" to descripcionLista
                        )

                        val db = FirebaseFirestore.getInstance()

                        db.collection("users").document(email).collection("listas").document(nombreLista)
                            .set(listaMap)
                            .addOnSuccessListener {
                                cargarNotasDesdeFirestore(email)
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error al crear la lista", e)
                            }
                    } else {
                        Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNeutralButton("Cancelar", null)
                .create()

            dialog.show()
        }

        private fun cargarNotasDesdeFirestore(email: String) {
            val db = FirebaseFirestore.getInstance()

            db.collection("users").document(email).collection("listas")
                .get()
                .addOnSuccessListener { result ->
                    listLista.clear()
                    for (document in result) {
                        val nombreLista = document.getString("nombre") ?: ""
                        val descripcionLista = document.getString("dia") ?: ""
                        val nuevaLista = Lista(nombreLista, descripcionLista.toInt())
                        listLista.add(nuevaLista)
                    }

                    listaAdapter = ListaAdapter(listLista, requireContext())
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    listaAdapter.notifyDataSetChanged()
                    binding.recyclerView.adapter = listaAdapter
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al obtener las notas", e)
                }
        }
    }
package com.example.listas_rgb

import Auxiliar.ConexionHuevo
import Conexion.AdminSQLiteConexionHuevo
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listas_rgb.databinding.FragmentHuevoBinding
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.grpc.Context
import kotlin.random.Random

class FragmentHuevo : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer
    lateinit var binding: FragmentHuevoBinding
    private var toques = 0
    private lateinit var txtToques: TextView
    private lateinit var imageViewHuevo: ImageView
    private lateinit var txtPuntuacion: TextView
    private lateinit var dbHelper: AdminSQLiteConexionHuevo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_huevo, container, false)
        binding = FragmentHuevoBinding.inflate(layoutInflater)

        imageViewHuevo = rootView.findViewById(R.id.imageView4)
        txtPuntuacion = rootView.findViewById(R.id.txtToques)
        val btnReiniciar = rootView.findViewById<Button>(R.id.btnReiJuego)

        // Inicializar MediaPlayer con el sonido
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.huevo_toque)
        dbHelper = AdminSQLiteConexionHuevo(requireContext())
        val puntuacionActual = dbHelper.getPuntuacion()
        ganador(puntuacionActual)
        if(puntuacionActual==1000){
            dbHelper.actualizarPuntuacion(0)
            imageViewHuevo.setImageResource(R.drawable.huevo1)

        }
        txtPuntuacion.text = "$puntuacionActual"
        // Configurar el evento de clic en la imagen del huevo
        imageViewHuevo.setOnClickListener {
            // Reproducir el sonido
            mediaPlayer.start()

            // Aumentar la puntuación
            val puntuacionActual = dbHelper.getPuntuacion()
            ganador(puntuacionActual)
            if(puntuacionActual==10001){
                dbHelper.actualizarPuntuacion(0)
                imageViewHuevo.setImageResource(R.drawable.huevo1)

            }
            val nuevaPuntuacion = puntuacionActual + 1
            dbHelper.actualizarPuntuacion(nuevaPuntuacion)

            // Actualizar la puntuación en el TextView
            txtPuntuacion.text = "Puntuación: $nuevaPuntuacion"

        }

        // Configurar el evento de clic en el botón Reiniciar
        btnReiniciar.setOnClickListener {
            // Reiniciar la puntuación en la base de datos
            dbHelper.actualizarPuntuacion(0)

            // Actualizar la puntuación en el TextView
            txtPuntuacion.text = "Puntuación: 0"
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
    }
    fun ganador(puntuacionActual : Int){
        if (puntuacionActual == 10000) {
            // Cambiar la imagen del ImageView
            imageViewHuevo.setImageResource(R.drawable.huevo2)

            // Reproducir otro sonido distinto
            val otroMediaPlayer = MediaPlayer.create(requireContext(), R.raw.pollito)
            otroMediaPlayer.start()

            // Mostrar un Toast
            Toast.makeText(requireContext(), "¡¡HAS DESCUBIERTO EL SECRETO!!", Toast.LENGTH_SHORT).show()
            //Notificacion

            mostrarNotificacion()


        }
    }
        @SuppressLint("MissingPermission")
        private fun mostrarNotificacion() {
            // Crear un Intent para abrir la actividad cuando se toque la notificación
            val intent = Intent(requireContext(), FragmentHuevo::class.java)
            val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)

            // Crear un objeto de notificación
            val builder = NotificationCompat.Builder(requireContext(), "canal_id")
                .setContentTitle("¡Descubriste el secreto!") // Título de la notificación
                .setContentText("¡Felicidades! Has descubierto el secreto.") // Texto de la notificación
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación
                .setContentIntent(pendingIntent) // Intent para abrir la actividad al tocar la notificación
                .setAutoCancel(true) // Cerrar la notificación al tocarla

            // Obtener el administrador de notificaciones
            val notificationManager = NotificationManagerCompat.from(requireContext())

            // Mostrar la notificación
            notificationManager.notify(1, builder.build())
        }
}


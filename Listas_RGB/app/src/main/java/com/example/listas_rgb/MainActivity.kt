package com.example.listas_rgb

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.firebase.Proveedor
import com.example.listas_rgb.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //Para la autenticación, de cualquier tipo.
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val TAG = "ACSCO"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "canal_id",
                    "Nombre del canal",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

        //Para la autenticación, de cualquier tipo.
        firebaseauth = FirebaseAuth.getInstance()
        //------------------------------ Autenticación con email y password ------------------------------------

        binding.btnAbre.setOnClickListener {
            showLoginDialog()
        }
        firebaseauth.signOut()
        //clearGooglePlayServicesCache()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btGoogle.setOnClickListener {
            loginEnGoogle()
        }


    }
    private fun loginEnGoogle(){
        //este método es nuestro.
        val signInClient = googleSignInClient.signInIntent
        launcherVentanaGoogle.launch(signInClient)
        //milauncherVentanaGoogle.launch(signInClient)
    }


    //con este launcher, abro la ventana que me lleva a la validacion de Google.
    private val launcherVentanaGoogle =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        //si la ventana va bien, se accede a las propiedades que trae la propia ventana q llamamos y recogemos en result.
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manejarResultados(task)
        }
    }

    //es como una muñeca rusa, vamos desgranando, de la ventana a task y de task a los datos concretos que me da google.
    private fun manejarResultados(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                actualizarUI(account)
            }
        }
        else {
            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //esta funcion actualiza o repinta la interfaz de usuario UI.
    private fun actualizarUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        //pido un token, y con ese token, si todo va bien obtengo la info.
        firebaseauth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                //hacer account. y ver otras propiedades interesantes.
                irHome(account.email.toString(),Proveedor.GOOGLE, account.displayName.toString())
            }
            else {
                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun irHome(email:String, provider: Proveedor, nombre:String = "Usuario"){
        Log.e(TAG,"Valores: ${email}, ${provider}, ${nombre}")
        val homeIntent = Intent(this, Home::class.java).apply {
            putExtra("email",email)
        }
        startActivity(homeIntent)
    }
    private fun showAlert(msg:String = "Se ha producido un error autenticando al usuario"){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    @SuppressLint("MissingInflatedId")
    private fun showLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.login_dialog, null)
        val edEmail = dialogView.findViewById<EditText>(R.id.edList)
        val edPass = dialogView.findViewById<EditText>(R.id.edDia)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Inicia Sesión con App")
            .setView(dialogView)
            .setPositiveButton("Registrarse") { _, _ ->
                // Aquí puedes manejar la lógica de inicio de sesión
                val email = edEmail.text.toString()
                val password = edPass.text.toString()
                if (password.isNotEmpty() && email.isNotEmpty()){
                    firebaseauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            //irHome(it.result?.user?.email?:"",Proveedor.BASIC)  //Esto de los interrogantes es por si está vacío el email, que enviaría una cadena vacía.
                        } else {
                            showAlert("Error registrando al usuario.")
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, "Error de autenticación: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    showAlert("Rellene los campos")
                }
            }
            .setNegativeButton("Inicia sesión") { _, _ ->
                // Aquí puedes manejar la lógica de inicio de sesión
                val email = edEmail.text.toString()
                val password = edPass.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()){
                    firebaseauth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            irHome(it.result?.user?.email?:"",Proveedor.BASIC)  //Esto de los interrogantes es por si está vacío el email.
                        } else {
                            showAlert()
                        }
                    }.addOnFailureListener{
                        Toast.makeText(this, "Conexión no establecida", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    showAlert("Rellene los campos")
                }
            }
            .setNeutralButton("Cancelar", null)
            .create()

        dialog.show()
    }
}
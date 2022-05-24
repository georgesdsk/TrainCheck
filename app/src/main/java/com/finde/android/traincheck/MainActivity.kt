package com.finde.android.traincheck

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.finde.android.traincheck.Activities.Sesion.ui.login.SignInActivity
import com.finde.android.traincheck.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 21

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mGruposRef: DatabaseReference
    private lateinit var mUsuariosRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView =findViewById<BottomNavigationView>(R.id.bottomNav)
        setupWithNavController(bottomNavigationView, navController)
        setupReferences()
        insertarGrupos() // y cambiar al entrenador
        setupAuth()

    }
//todo como conseguir el uid
    private fun insertarGrupos() {
       // mFirebaseAuth.createUserWithEmailAndPassword("user@gmail.com", "user213").addOnCompleteListener {
            mGruposRef.child("Alumnos").setValue("BwUe4AhTQGfknuDumtQKV8bA9Kq1")
            mGruposRef.child("Entrenadores").setValue("kd4PRHCMbXRrkOn6gjGEi88astO2")
    }


    private fun setupReferences() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app").reference
        mUsuariosRef = mDatabaseRef.child("Usuarios")
        mGruposRef = mDatabaseRef.child("Grupos")
    }


    private fun setupAuth(){
        //necesitas un listener por si sales en cualquier momento

        var entrenadores = mutableListOf<String>()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if(user == null){
                val intent = Intent(this, SignInActivity::class.java)
                this.startActivity(intent)

            //esto podria ser un .get pero no funciona mGruposRef.child("Entrenadores").get(mFirebaseAuth.currentUser!!.uid)
            mGruposRef.child("Entrenadores").equalTo(mFirebaseAuth.currentUser!!.uid)

            val entrenadorListener = object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    entrenadores = snapshot.getValue<MutableList<String>>
                    println(entrenadores[0])
                    println(entrenadores[0])
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }

            mGruposRef.child("Entrenadores").addListenerForSingleValueEvent(entrenadorListener)





        }


    }

    //doble pantalla , nombre, apellido, correo, codigo, contraseña
    // recoger los datos de los dos fragments

     private fun register(){

         mFirebaseAuth.createUserWithEmailAndPassword("email@gmail.com","contraseña").addOnCompleteListener{
             if (it.isSuccessful){
                mDatabaseRef
             }
         }
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Bienvenido...", Toast.LENGTH_SHORT).show()
            } else {
                if (IdpResponse.fromResultIntent(data) == null){ // si el boton de atras se ha pulsado
                    finish()
                }
            }
        }
    }
}
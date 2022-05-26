package com.finde.android.traincheck.Fragments.main_page

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.finde.android.traincheck.Activities.Sesion.ui.login.SignInActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.databinding.ActivityMainBinding
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.fragment.app.activityViewModels


class MainActivity : AppCompatActivity() {

    //todo
    /*override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                          Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)*/

    private val RC_SIGN_IN = 21

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mGruposRef: DatabaseReference
    private lateinit var mUsuariosRef: DatabaseReference
    private val grupoSeleccionado: GrupoSeleccionado by viewModels<GrupoSeleccionado>()
    var entrenadores = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupNavigationBar()
        setupHeaderNav()
        setupReferences()
        setupAuth()
        // insertarGrupos() // y cambiar al entrenador
    }

    private fun setupHeaderNav() {
        mBinding.headerNav.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //Toast.makeText(this@MainActivity, "Seleccionado", Toast.LENGTH_SHORT).show()
                when (tab?.position) {
                    0 -> {
                        grupoSeleccionado.grupoSeleccionado = "0"
                        Toast.makeText(this@MainActivity, "0", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        grupoSeleccionado.grupoSeleccionado = "1"
                        Toast.makeText(this@MainActivity, "Seleccionado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }


    private fun setupNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        setupWithNavController(bottomNavigationView, navController)

    }

    //todo como conseguir el uid
    private fun insertarGrupos() {
        // mFirebaseAuth.createUserWithEmailAndPassword("user@gmail.com", "user213").addOnCompleteListener {
        mGruposRef.child("Alumnos").setValue("pof")
        mGruposRef.child("Entrenadores").child("ponoje").setValue("kd4PRHChgMbXRrkOn6gjGEi88astO2")
    }


    private fun setupReferences() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef =
            FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app").reference
        mUsuariosRef = mDatabaseRef.child("Usuarios")
        mGruposRef = mDatabaseRef.child("Grupos")
    }


    private fun setupAuth() {
        //necesitas un listener por si sales en cualquier momento
        var isEntrenador: Boolean = false
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user == null) {
                val intent = Intent(this, SignInActivity::class.java)
                this.startActivity(intent)
            }
            //esto podria ser un .get pero no funciona mGruposRef.child("Entrenadores").get(mFirebaseAuth.currentUser!!.uid)
            // mGruposRef.child("Entrenadores").equalTo(mFirebaseAuth.currentUser!!.uid)

            val entrenadorListener = object : ValueEventListener {
                override fun onDataChange(snapshots: DataSnapshot) {
                    isEntrenador = false
                    for (snapshot in snapshots.children) { // ponerle un and si se ha encontradon // utilizaria lambda pero los snapshots no son string
                        if (snapshot.getValue().toString() == mFirebaseAuth.currentUser!!.uid) {
                            isEntrenador = true
                        }
                    }
                    if (isEntrenador) {
                        iniciarAlumno()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            mGruposRef.child("Entrenadores").addListenerForSingleValueEvent(entrenadorListener)
        }

    }


    //Analisis: cambiar el navBar, navBar superior,  boton de entrenamientos, y stas solo particulares
    private fun iniciarAlumno() {
        mBinding.bottomNav.menu.removeItem(R.id.asistFragment)
        //mBinding.topNav.visibility = View.GONE
    }

//doble pantalla , nombre, apellido, correo, codigo, contraseña
// recoger los datos de los dos fragments

    private fun register() {

        mFirebaseAuth.createUserWithEmailAndPassword("email@gmail.com", "contraseña")
            .addOnCompleteListener {
                if (it.isSuccessful) {
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
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bienvenido...", Toast.LENGTH_SHORT).show()
            } else {
                if (IdpResponse.fromResultIntent(data) == null) { // si el boton de atras se ha pulsado
                    finish()
                }
            }
        }
    }
}
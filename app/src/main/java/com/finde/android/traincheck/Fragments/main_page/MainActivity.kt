package com.finde.android.traincheck.Fragments.main_page

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.cursosant.android.traincheck.Entities.Group
import com.finde.android.traincheck.login.SignInActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.databinding.ActivityMainBinding
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mDatabaseRef
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mEntrenadoresRef
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mFirebaseAuth
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mGruposRef
import com.google.firebase.ktx.Firebase
import kotlin.math.sign


class MainActivity : AppCompatActivity() {

    //todo
    /*override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                          Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)*/

    private val RC_SIGN_IN = 21
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var navController: NavController
    private val grupoSeleccionado: GrupoSeleccionado by viewModels<GrupoSeleccionado>()
    var entrenadores = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        grupoSeleccionado.currentGroup.value = "AltoRendimiento"
        FireBaseReferencies.create()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupNavigationBar()
        setupHeaderNav()
        setupAuth()
        Toast.makeText(this, grupoSeleccionado.currentGroup.toString(), Toast.LENGTH_SHORT).show()
        // insertarGrupos() // y cambiar al entrenador
    }

    private fun setupHeaderNav() {
        mBinding.headerNav.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //Toast.makeText(this@MainActivity, "Seleccionado", Toast.LENGTH_SHORT).show()

                when (tab?.position) {
                    0 -> {
                        grupoSeleccionado.currentGroup.value = "Formacion"
                    }
                    1 -> {
                        grupoSeleccionado.currentGroup.value = "Altorendimiento"
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


    private fun setupNavigationBar() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = mBinding.bottomNav //findViewById<BottomNavigationView>(R.id.bottomNav)
        setupWithNavController(bottomNavigationView, navController)

    }


    private fun setupAuth() {
        //necesitas un listener por si sales en cualquier momento
        val entrenadorListener = object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                var isEntrenador: Boolean = false
                if (mFirebaseAuth.currentUser != null) {
                    for (snapshot in snapshots.children) { // ponerle un and si se ha encontradon // utilizaria lambda pero los snapshots no son string
                        if (mFirebaseAuth.currentUser!!.uid == snapshot.key) {
                            isEntrenador = true
                        }
                    }
                    if (isEntrenador) {
                        signIn()
                    }
                    else{
                        iniciarAlumno()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        mEntrenadoresRef.addListenerForSingleValueEvent(entrenadorListener)

        mAuthListener = FirebaseAuth.AuthStateListener {
            signIn()
        }

    }


    private fun signIn() {
        val user = mFirebaseAuth.currentUser
        if (user == null) {
            val intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }
    }

    //Analisis: cambiar el navBar, navBar superior,  boton de entrenamientos, y stas solo particulares
    private fun iniciarAlumno() {
        mBinding.bottomNav.menu.removeItem(R.id.asistFragment)
        mBinding.headerNav.visibility = TabLayout.GONE
        //encontra su grupo
        findGroup()
        signIn()
        //mBinding.topNav.visibility = View.GONE
    }

    private fun findGroup(){
        val gruposListener = object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                for (snapshot in snapshots.children) {
                    val group = snapshot.getValue(Group::class.java)
                    for(athlet in group!!.listaAtletas)
                    {
                        if(athlet.id == mFirebaseAuth.currentUser!!.uid)
                        {
                            grupoSeleccionado.currentGroup.value = group.name
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        mGruposRef.addListenerForSingleValueEvent(gruposListener)
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
//todo quitar y anhadir los listeners
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
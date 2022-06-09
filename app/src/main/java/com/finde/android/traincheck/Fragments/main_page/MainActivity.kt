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
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.login.SignInActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.databinding.ActivityMainBinding
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mAtletasRef
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mDatabaseRef
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mEntrenadoresRef
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mFirebaseAuth
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mGruposRef
import com.finde.android.traincheck.ViewModel.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {


    private val RC_SIGN_IN = 21
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var navController: NavController
    private val grupoSeleccionado: GrupoSeleccionado by viewModels<GrupoSeleccionado>()
    var isEntrenador: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //setAthlets()

        FireBaseReferencies.create()

        if(grupoSeleccionado.currentGroup.value==null){
            grupoSeleccionado.currentGroup.value = "AltoRendimiento"
        }

        signIn()
        setupAuthListeners()
        if (!isEntrenador) {
            UserType.type = "Atleta"
            findGroup()
        }
        setupNavigationBar()
        setupHeaderNav()
        findGroup()


        Toast.makeText(this, grupoSeleccionado.currentGroup.value, Toast.LENGTH_SHORT).show()
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
                        grupoSeleccionado.currentGroup.value = "AltoRendimiento"
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
        val bottomNavigationView =
            mBinding.bottomNav //findViewById<BottomNavigationView>(R.id.bottomNav)
        setupWithNavController(bottomNavigationView, navController)

    }


    private fun setupAuthListeners() {
        //necesitas un listener por si sales en cualquier momento
        val entrenadorListener = object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {

                if (mFirebaseAuth.currentUser != null) {
                    for (snapshot in snapshots.children) { // ponerle un and si se ha encontradon // utilizaria lambda pero los snapshots no son string
                        if (mFirebaseAuth.currentUser!!.uid == snapshot.key) {
                            isEntrenador = true
                        }
                    }
                    if (isEntrenador) {
                        grupoSeleccionado.currentGroup.value = "Formacion"
                        UserType.type = "Entrenador"
                    } else {
                        iniciarAlumno()
                        UserType.type = "Atleta"
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

    //me hace el find antes de iniciar sesion
    private fun findGroup() {
        val athletsListener = object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                for (snapshot in snapshots.children) {
                    val athlet = snapshot.getValue(Athlet::class.java)
                    if (athlet!!.id == mFirebaseAuth.currentUser!!.uid) {
                        grupoSeleccionado.currentGroup.value = athlet.group
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        mAtletasRef.addListenerForSingleValueEvent(athletsListener)

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

    private fun setAthlets() {


        var atleta = Athlet(
            name = "Maria",
            surname = "Fernandez Pastor",
            id = "BzgUCsXvQgY04GByJzoPfM2La832",
            group = "AltoRendimiento",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://socialtools.me/wp-content/uploads/2016/04/foto-de-perfil.jpg"
        )

        var atleta2 = Athlet(
            name = "Juan ",
            surname = "Jimenez franco",
            id = "b76taXe0sdfAh2MMZBFwi6DN7Ch1",
            group = "AltoRendimiento",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://iteragrow.com/wp-content/uploads/2018/04/buyer-persona-e1545248524290.jpg"
        )
        var atleta3 = Athlet(
            name = "Juan",
            surname = "Fernandez Pastor",
            id = "7JWFPG1QeEd3DDaAJ9UlceScGHi1",
            group = "Formacion",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://definicionde.es/wp-content/uploads/2019/04/definicion-de-persona-min.jpg"
        )

        var atleta4 = Athlet(
            name = "Juan alberto",
            surname = "Jimenez franco",
            id = "Hezl97qqbPNPFPSAdG93FWRlhhB3",
            group = "Formacion",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://pymstatic.com/44253/conversions/xavier-molina-medium.jpg"
        )
        var atleta5 = Athlet(
            name = "Juan",
            surname = "Fernandez Pastor",
            id = "BwUe4AhTQGfknuDumtQKV8bA9Kq1",
            group = "AltoRendimiento",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://dam.muyinteresante.com.mx/wp-content/uploads/2018/05/fotos-de-perfil-son-mejor-elegidas-por-personas-extranas-afirma-estudio.jpg"
        )

        var atleta6 = Athlet(
            name = "Juan alberto",
            surname = "Jimenez franco",
            id = "ItbcuhEjEzdZGGjFu8pjACMLdUz1",
            group = "AltoRendimiento",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://cdn.eldeforma.com/wp-content/uploads/2020/01/images-1-6.jpg"
        )
        FireBaseReferencies.mAtletasRef.child(atleta.id).setValue(atleta)
        FireBaseReferencies.mAtletasRef.child(atleta2.id).setValue(atleta2)
        FireBaseReferencies.mAtletasRef.child(atleta3.id).setValue(atleta3)
        FireBaseReferencies.mAtletasRef.child(atleta4.id).setValue(atleta4)
        FireBaseReferencies.mAtletasRef.child(atleta5.id).setValue(atleta5)
        FireBaseReferencies.mAtletasRef.child(atleta6.id).setValue(atleta6)
        FireBaseReferencies.mAtletasRef.child(atleta.id).setValue(atleta)
        FireBaseReferencies.mAtletasRef.child(atleta2.id).setValue(atleta2)

    }


}
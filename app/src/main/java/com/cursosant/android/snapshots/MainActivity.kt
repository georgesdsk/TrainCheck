package com.cursosant.android.snapshots

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cursosant.android.snapshots.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 21

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFirebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //setupAuth()
        setupBottomNav()
    }

    private fun setupAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user == null){
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                                Arrays.asList(
                                        AuthUI.IdpConfig.EmailBuilder().build(),
                                        AuthUI.IdpConfig.GoogleBuilder().build())
                        )
                        .build(), RC_SIGN_IN)
            }
        }
    }

    private fun setupBottomNav(){
        mFragmentManager = supportFragmentManager

        val asistFragment = AsistFragment()
        val trainingFragment = TrainingFragment()
        val statsFragment = StatsFragment()

        mActiveFragment = asistFragment

        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment, statsFragment, StatsFragment::class.java.name)
            .hide(statsFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment, trainingFragment, TrainingFragment::class.java.name)
            .hide(trainingFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment, asistFragment, AsistFragment::class.java.name).commit()

        mBinding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.asistencia -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(asistFragment).commit()
                    mActiveFragment = asistFragment
                    true
                }
                R.id.entrenos -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(trainingFragment).commit()
                    mActiveFragment = trainingFragment
                    true
                }
                R.id.stats -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(statsFragment).commit()
                    mActiveFragment = statsFragment
                    true
                }
                else -> false
            }
        }

        mBinding.bottomNav.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.entrenos -> (asistFragment as HomeAux).goToTop()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth?.addAuthStateListener(mAuthListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth?.removeAuthStateListener(mAuthListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Bienvenido...", Toast.LENGTH_SHORT).show()
            } else {
                if (IdpResponse.fromResultIntent(data) == null){
                    finish()
                }
            }
        }
    }
}
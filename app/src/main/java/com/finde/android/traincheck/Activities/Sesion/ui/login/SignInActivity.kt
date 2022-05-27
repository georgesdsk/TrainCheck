package com.finde.android.traincheck.Activities.Sesion.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.finde.android.traincheck.Fragments.main_page.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.databinding.ActivitySignInBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//ocultar el nombre
class SignInActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mBinding: ActivitySignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mFirebaseAuth = FirebaseAuth.getInstance()

        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.registerbtn.setOnClickListener {
            initRegister()
        }

        mBinding.loginbtn
            .setOnClickListener {
            iniciarSesion("sadachok1999@gmai.com","123456")
        }

    }




    private fun initRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        this.startActivity(intent)
    }

    private fun iniciarSesion(s: String, s1: String) {
        mFirebaseAuth.signInWithEmailAndPassword(s, s1).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "signInWithEmail:success")
                reload()
            } else {
                Log.w("TAG", "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reload (){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }


}
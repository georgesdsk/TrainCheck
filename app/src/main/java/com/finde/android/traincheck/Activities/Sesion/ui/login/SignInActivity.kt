package com.finde.android.traincheck.Activities.Sesion.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.finde.android.traincheck.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignInActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mBinding: ActivitySignInBinding
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mGruposRef: DatabaseReference
    private lateinit var mUsuariosRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mFirebaseAuth = FirebaseAuth.getInstance()

        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        
        mBinding.button1.setOnClickListener {
            iniciarSesion("sadachok1999@gmai.com","123456")
        }
        mBinding.button3.setOnClickListener {
            iniciarSesion("sadachok99@gmai.com","123456")

        }
    }

    private fun setupReferences() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app").reference
        mUsuariosRef = mDatabaseRef.child("Usuarios")
        mGruposRef = mDatabaseRef.child("Grupos")

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
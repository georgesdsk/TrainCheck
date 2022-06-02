package com.finde.android.traincheck.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.finde.android.traincheck.Fragments.main_page.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.databinding.ActivitySignInBinding
import com.finde.android.traincheck.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

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
            iniciarSesion(mBinding.username.text.toString(), mBinding.password.text.toString()  )
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
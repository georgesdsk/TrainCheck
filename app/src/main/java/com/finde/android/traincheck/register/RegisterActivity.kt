package com.finde.android.traincheck.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.NavHostFragment
import com.finde.android.traincheck.Fragments.main_page.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mFirebaseAuth
import com.finde.android.traincheck.ViewModel.RegistrationViewModel
import com.finde.android.traincheck.databinding.RegistryActivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: RegistryActivityBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = RegistryActivityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }


    private fun setupHeaderNav() {
/*        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.registryContainer) as NavHostFragment
        navController = navHostFragment.navController*/


        mBinding.headerNav.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //Toast.makeText(this@MainActivity, "Seleccionado", Toast.LENGTH_SHORT).show()

                when (tab?.position) {
                    0 -> {

                    }
                    1 -> {

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}
package com.finde.android.traincheck.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.finde.android.traincheck.ViewModel.RegistrationViewModel
import com.finde.android.traincheck.databinding.RegistryActivityBinding
import com.google.android.material.tabs.TabLayout

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
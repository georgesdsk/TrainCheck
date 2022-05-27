package com.finde.android.traincheck.Activities.Sesion.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.finde.android.traincheck.databinding.ActivitySignInBinding
import com.finde.android.traincheck.databinding.RegistryActivityBinding
import com.google.android.material.tabs.TabLayout

class RegisterActivity: AppCompatActivity() {

    private lateinit var mBinding: RegistryActivityBinding

         override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mBinding = RegistryActivityBinding.inflate(layoutInflater)
            setContentView(mBinding.root)
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




    private fun setupDatePickerEditText(){
        mBinding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day: Int, month: Int, year: Int ->
            onDateSelected(
                day,
                month,
                year
            )
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        mBinding.datePicker.setText("$day/$month/$year")
    }

}
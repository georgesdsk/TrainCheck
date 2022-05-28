package com.finde.android.traincheck.register

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.finde.android.traincheck.Fragments.main_page.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.RegistrationViewModel
import com.finde.android.traincheck.databinding.FragmentRegister1Binding

class Register1 : Fragment() {

    private lateinit var mBinding: FragmentRegister1Binding
    private val registrationViewModel: RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentRegister1Binding.inflate(inflater, container, false)
        return mBinding.root

    }


    private fun setupEditText() {

        mBinding.buttonNext.setOnClickListener {
            registrationViewModel.name.postValue(mBinding.name.text.toString())
            registrationViewModel.surname.postValue(mBinding.surname.text.toString())
            registrationViewModel.nGroup.postValue(mBinding.group.text.toString())
            registrationViewModel.mail.postValue(mBinding.mail.text.toString())
            registrationViewModel.password.postValue(mBinding.password.text.toString())
            registrationViewModel.password2.postValue(mBinding.password2.text.toString())
            // val image = mBinding.photo.text
            // val date = mBinding.group.text

            if (registrationViewModel.name.value!!.isEmpty() || registrationViewModel.surname.value!!.isEmpty() ||
                registrationViewModel.nGroup.value!!.isEmpty() || registrationViewModel.mail.value!!.isEmpty() ||
                registrationViewModel.password.value!!.isEmpty() || registrationViewModel.password2.value!!.isEmpty()) {
                Toast.makeText(context, "All data is required", Toast.LENGTH_SHORT).show()
            } else {
                if (password == password2) {
                    FireBaseReferencies.mFirebaseAuth.createUserWithEmailAndPassword(
                        mail.toString(),
                        password.toString()
                    ).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "createUserWithEmail:success")

                            //createUserOnDatabase(name, surname, numGroup, mail, mFirebaseAuth.currentUser!!.uid)
                            reload()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }
        }

        /*if (TextUtils.isEmpty(mBinding.name.text)) {
            mBinding.name.setError("");
            return;
        }

        mBinding.name.doOnTextChanged { text, start, before, count ->
            registrationViewModel.name.postValue(text as String?)
        }
        mBinding.surname.doOnTextChanged { text, start, before, count ->
            registrationViewModel.name.postValue(text as String?)
        }
        mBinding.group.doOnTextChanged { text, start, before, count ->
            registrationViewModel.name.postValue(text as String?)
        }
        mBinding.datePicker.doOnTextChanged { text, start, before, count ->
            registrationViewModel.name.postValue(text as String?)
        }
        mBinding.password.doOnTextChanged { text, start, before, count ->
            registrationViewModel.name.postValue(text as String?)
        }
        */

    }



    private fun setupDatePickerEditText() {
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

    private fun reload (){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnclickListeners()
    }

    private fun setupOnclickListeners() {

        val navigation = Navigation.findNavController(mBinding.root)
        mBinding.loginbtn.setOnClickListener { navigation.navigate(R.id.action_register1_to_register2) }
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
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        mBinding.datePicker.setText("$day/$month/$year")
    }



}
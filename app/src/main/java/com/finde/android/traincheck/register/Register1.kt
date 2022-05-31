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
import androidx.core.text.set
import androidx.core.widget.doOnTextChanged
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnclickListeners()
        setupEditText()
        setupInputText()
        registrationViewModel
        mBinding.name.text.toString()
    }

    private fun setupEditText() {
        // error de que l veiwmodole no esta iniciado

        mBinding.buttonNext.setOnClickListener {
            passToNextFragment()
        }
        mBinding.datePicker.setOnClickListener {
            setupDatePickerEditText()
        }
    }

    private fun passToNextFragment() {
        setupInputText()
        Navigation.findNavController(mBinding.root).navigate(R.id.action_register1_to_register2)
    }

    private fun setupInputText() {
        var name =  mBinding.name.text.toString()
        var surname =  mBinding.surname.text.toString()
        var group =  mBinding.group.text.toString()
        var mail =  mBinding.mail.text.toString()
        var password =  mBinding.password.text.toString()
        var password2 =  mBinding.password2.text.toString()

        if (name.isNotEmpty()){
            registrationViewModel.name.postValue(name)
        }else{
            mBinding.name.setText(registrationViewModel.name.value.toString())
        }

        if (surname.isNotEmpty()){
            registrationViewModel.surname.postValue(surname)
        }else{
            mBinding.surname.setText(registrationViewModel.surname.value.toString())
        }
        if (group.isNotEmpty()){
            registrationViewModel.nGroup.postValue(group)
        }else{
            mBinding.group.setText(registrationViewModel.nGroup.value.toString())
        }
        if (mail.isNotEmpty()){
            registrationViewModel.mail.postValue(mail)
        }else{
            mBinding.mail.setText(registrationViewModel.mail.value.toString())
        }

        if (password.isNotEmpty()){
            registrationViewModel.password.postValue(password)
        }else{
            mBinding.password.setText(registrationViewModel.password.value.toString())
        }
        if (password2.isNotEmpty()){
            registrationViewModel.password2.postValue(password2)
        }else{
            mBinding.password2.setText(registrationViewModel.password2.value.toString())
        }


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
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        mBinding.datePicker.setText("$day/$month/$year")
    }

    private fun setupOnclickListeners() {

        val navigation = Navigation.findNavController(mBinding.root)
        mBinding.buttonNext.setOnClickListener { navigation.navigate(R.id.action_register1_to_register2) }
    }




}
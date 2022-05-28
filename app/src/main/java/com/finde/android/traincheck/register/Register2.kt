package com.finde.android.traincheck.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.finde.android.traincheck.R

import com.finde.android.traincheck.databinding.FragmentRegister2Binding

class Register2 : Fragment() {

    private lateinit var mBinding: FragmentRegister2Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentRegister2Binding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnclickListeners()
    }



    private fun setupOnclickListeners() {

        val navigation = Navigation.findNavController(mBinding.root)
        mBinding.backButton.setOnClickListener { navigation.navigate(R.id.action_register2_to_register1) }
        mBinding.buttonNext.setOnClickListener {  }
    }



}
package com.finde.android.traincheck.Fragments.main_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finde.android.traincheck.databinding.FragmentInicioBinding

class InicioFragment : Fragment() {

    private lateinit var mBinding: FragmentInicioBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentInicioBinding.inflate(inflater, container, false)
        return mBinding.root

    }

}
package com.finde.android.traincheck.Fragments.stats

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.finde.android.traincheck.Fragments.main_page.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.ViewModel.VmEstadisticas
import com.finde.android.traincheck.databinding.FragmentStatsBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class StatsFragment : Fragment() {

    private lateinit var mBinding: FragmentStatsBinding
    private val vmEstadisticas: VmEstadisticas by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStatsBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.tvName.text = FirebaseAuth.getInstance().currentUser?.displayName
        mBinding.tvEmail.text = FirebaseAuth.getInstance().currentUser?.email
        val navigation = Navigation.findNavController(mBinding.root)

        mBinding.btnEncuesta.setOnClickListener{
            navigation.navigate(R.id.action_statsFragment_to_graphicsFragment)

        }
        mBinding.btnLogout.setOnClickListener{
            singOut()
        }

        mBinding.esfuerzo.setOnClickListener { navigation.navigate(R.id.action_statsFragment_to_graphicsFragment) }
        mBinding.resultados.setOnClickListener { navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }
        mBinding.motivacion.setOnClickListener { navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }
        mBinding.cansancio.setOnClickListener { navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }



    }

    private fun singOut() {
        context?.let {
            AuthUI.getInstance().signOut(it)
                    .addOnCompleteListener {
                        Toast.makeText(context, "Hasta pronto...", Toast.LENGTH_SHORT).show()
                    }
        }
    }
}
package com.finde.android.traincheck.Fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.databinding.FragmentStatsBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class StatsFragment : Fragment() {

    private lateinit var mBinding: FragmentStatsBinding

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
            navigation.navigate(R.id.actionEncuesta)

        }
        mBinding.btnLogout.setOnClickListener{
            singOut()
        }

        Toast.makeText(context, Toast.LENGTH_LONG).show()

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
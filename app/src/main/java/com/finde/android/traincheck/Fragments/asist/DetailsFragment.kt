package com.finde.android.traincheck.Fragments.asist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.finde.android.traincheck.ViewModel.AtletaSeleccionado
import com.finde.android.traincheck.databinding.FragmentAtletaDetallesBinding



class DetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentAtletaDetallesBinding
    private val vmAtleta: AtletaSeleccionado by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAtletaDetallesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

//todo EL grupo y las faltas

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombreCompleto: String = vmAtleta.atletaSeleccionado.nombre + vmAtleta.atletaSeleccionado.apellidos

        mBinding.tituloNombre.text = nombreCompleto

        Glide.with(this)
            .load(vmAtleta.atletaSeleccionado.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imagen)

    }


}
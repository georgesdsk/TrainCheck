package com.finde.android.traincheck.Fragments.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.finde.android.traincheck.ViewModel.AtletaSeleccionado
import com.finde.android.traincheck.ViewModel.SelectedTraining
import com.finde.android.traincheck.databinding.FragmentAtletaDetallesBinding
import com.finde.android.traincheck.databinding.FragmentTrainingDetailsBinding

//todo anhadir boton atras

class TrainingDetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentTrainingDetailsBinding
    private val vmTraining: SelectedTraining by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTrainingDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

//todo EL grupo y las faltas

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mBinding.tituloNombre.text = vmTraining.selectedTraining.nombre
        mBinding.tituloCategoria.text = vmTraining.selectedTraining.group

        Glide.with(this)
            .load(vmTraining.selectedTraining.urlEntrenamiento)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imagen)

    }


}
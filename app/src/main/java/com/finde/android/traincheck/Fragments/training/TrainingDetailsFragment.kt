package com.finde.android.traincheck.Fragments.training

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.finde.android.traincheck.Entities.Entrenamiento
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.AtletaSeleccionado
import com.finde.android.traincheck.ViewModel.SelectedTraining
import com.finde.android.traincheck.databinding.FragmentAtletaDetallesBinding
import com.finde.android.traincheck.databinding.FragmentTrainingDetailsBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.text.SimpleDateFormat

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

        val format = SimpleDateFormat("dd/MM/yyyy")

        mBinding.tvNombre.text = vmTraining.selectedTraining.nombre
        mBinding.grupo.text = vmTraining.selectedTraining.group
        mBinding.fechaTitulo.text = format.format( vmTraining.selectedTraining.fecha)

        Glide.with(this)
            .load(vmTraining.selectedTraining.urlEntrenamiento)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(object: CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    mBinding.imagen.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            });



    }


}
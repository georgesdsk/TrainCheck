package com.finde.android.traincheck.Fragments.training

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.finde.android.traincheck.Entities.Entrenamiento
import com.finde.android.traincheck.Fragments.asist.AsistFragment
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mGruposRef
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.databinding.FragmentTrainingBinding
import com.finde.android.traincheck.databinding.ItemAtletaBinding
import com.finde.android.traincheck.databinding.ItemEntrenamientoBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class TrainingFragment : Fragment() {

    private val RC_GELLERY = 18
    private val PATH_TRAININGS = "trainings"
    private lateinit var mBinding: FragmentTrainingBinding
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Entrenamiento, TrainingFragment.TrainingHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val grupoSeleccionado: GrupoSeleccionado by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mBinding = FragmentTrainingBinding.inflate(inflater, container, false)
        mBinding.fab.setOnClickListener {
            Navigation.findNavController(mBinding.root).navigate(R.id.navigateToAdd)
        }

        setListener()
        setupAdapter()
        setExample()
        setupRecyclerView()

        return mBinding.root
    }

    private fun setExample() {
        FireBaseReferencies.mGruposRef.child(grupoSeleccionado.currentGroup.value!!).child("Entrenamientos")
            .child("ciuaqluier1").setValue(Entrenamiento(urlEntrenamiento = "https://www.adslzone.net/app/uploads-adslzone.net/2019/04/borrar-fondo-imagen.jpg"))
        FireBaseReferencies.mGruposRef.child(grupoSeleccionado.currentGroup.value!!).child("Entrenamientos")
            .child("ciuaqluier2").setValue(Entrenamiento(urlEntrenamiento = "https://www.adslzone.net/app/uploads-adslzone.net/2019/04/borrar-fondo-imagen.jpg"))
        FireBaseReferencies.mGruposRef.child(grupoSeleccionado.currentGroup.value!!).child("Entrenamientos")
            .child("ciuaqluier3").setValue(Entrenamiento(urlEntrenamiento = "https://www.adslzone.net/app/uploads-adslzone.net/2019/04/borrar-fondo-imagen.jpg"))
        FireBaseReferencies.mGruposRef.child(grupoSeleccionado.currentGroup.value!!).child("Entrenamientos")
            .child("ciuaqluier4").setValue(Entrenamiento(urlEntrenamiento = "https://www.adslzone.net/app/uploads-adslzone.net/2019/04/borrar-fondo-imagen.jpg"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setListener(){
        grupoSeleccionado.currentGroup.observe(viewLifecycleOwner, {
            Toast.makeText(getActivity(),grupoSeleccionado.currentGroup.value, Toast.LENGTH_SHORT  )
            val grupo  = grupoSeleccionado.currentGroup.value!!
            val options =
                FirebaseRecyclerOptions.Builder<Entrenamiento>().setQuery(mGruposRef.child(grupo).child("Entrenamientos"), Entrenamiento::class.java)
                    .build()
            mFirebaseAdapter.updateOptions(options)
            //mFirebaseAdapter.notifyDataSetChanged()
        })
    }

    private fun setupAdapter() {
        val grupo  = grupoSeleccionado.currentGroup.value!!
        val options =
            FirebaseRecyclerOptions.Builder<Entrenamiento>().setQuery(mGruposRef.child(grupo).child("Entrenamientos"), Entrenamiento::class.java)
                .build()


        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Entrenamiento, TrainingFragment.TrainingHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingFragment.TrainingHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_entrenamiento, parent, false)
                return TrainingHolder(view)
            }


            override fun onBindViewHolder(holder: TrainingFragment.TrainingHolder, position: Int, model: Entrenamiento) {
                val entrenamiento = getItem(position)

                with(holder) {
                    setListener(entrenamiento)
                    val completo = entrenamiento.nombre
                    binding.tvName.text = completo
                    Glide.with(mContext)
                        .load(entrenamiento.urlEntrenamiento)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imgStore)
                }
            }

            @SuppressLint("NotifyDataSetChanged")//error interno firebase ui 8.0.0
            override fun onDataChanged() {
                super.onDataChanged()
                mBinding.progressBar.visibility = View.GONE
                notifyDataSetChanged()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Snackbar.make(mBinding.root, error.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        mLayoutManager = GridLayoutManager(context, 2)

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

     fun goToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

    private fun deleteEntrenamiento(entrenamiento: Entrenamiento) {
        mGruposRef.child(entrenamiento.id).removeValue()}

        //todo al hacer el click largo podamos modificar al atleta
        inner class TrainingHolder(view: View) : RecyclerView.ViewHolder(view) {
            val binding = ItemEntrenamientoBinding.bind(view)
            val navigation = Navigation.findNavController(mBinding.root)

            fun setListener(entrenamiento: Entrenamiento) {
                with(binding.root) {
                    setOnClickListener {
                        Toast.makeText(context, entrenamiento.id, Toast.LENGTH_SHORT).show()

                        navigation.navigate(R.id.actionAthletDetails)
                    }
                    setOnLongClickListener {
                        deleteEntrenamiento(entrenamiento)
                        true
                    }
                }

            }
        }






}
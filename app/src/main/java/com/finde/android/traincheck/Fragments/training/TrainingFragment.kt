package com.finde.android.traincheck.Fragments.training

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.finde.android.traincheck.Entities.Training
import com.finde.android.traincheck.R
import com.finde.android.traincheck.DAL.FireBaseReferencies.Companion.mGruposRef
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.ViewModel.SelectedTraining
import com.finde.android.traincheck.ViewModel.UserType
import com.finde.android.traincheck.databinding.FragmentTrainingBinding
import com.finde.android.traincheck.databinding.ItemEntrenamientoBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat

//todo meter el delete cuando ingrese nuevos entrenamientos
class TrainingFragment : Fragment() {


    private lateinit var mBinding: FragmentTrainingBinding
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Training, TrainingFragment.TrainingHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private val grupoSeleccionado: GrupoSeleccionado by activityViewModels()
    private val selectedTraining: SelectedTraining by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTrainingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setUI()
        setupAdapter()
        //setExample()
        setupRecyclerView()

    }

    private fun setUI() {
        if (UserType.type == "Atleta") {
            mBinding.fab.visibility = View.GONE
        }
    }

    private fun setListener() {
        grupoSeleccionado.currentGroup.observe(viewLifecycleOwner, {
            Toast.makeText(getActivity(), grupoSeleccionado.currentGroup.value, Toast.LENGTH_SHORT)
            val grupo = grupoSeleccionado.currentGroup.value!!
            val options =
                FirebaseRecyclerOptions.Builder<Training>().setQuery(
                    mGruposRef.child(grupo).child("Entrenamientos"),
                    Training::class.java
                )
                    .build()
            mFirebaseAdapter.updateOptions(options)
            //mFirebaseAdapter.notifyDataSetChanged()
        })
        mBinding.fab.setOnClickListener {
            Navigation.findNavController(mBinding.root).navigate(R.id.navigateToAdd)
        }


    }

    private fun setupAdapter() {

        val grupo = grupoSeleccionado.currentGroup.value!!

        val options =
            FirebaseRecyclerOptions.Builder<Training>().setQuery(
                mGruposRef.child(grupo).child("Entrenamientos"),
                Training::class.java
            ).build()

        mFirebaseAdapter = object :
            FirebaseRecyclerAdapter<Training, TrainingFragment.TrainingHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): TrainingFragment.TrainingHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_entrenamiento, parent, false)
                return TrainingHolder(view)
            }


            override fun onBindViewHolder(
                holder: TrainingFragment.TrainingHolder,
                position: Int,
                model: Training
            ) {
                val entrenamiento = getItem(position)
                val sdf = SimpleDateFormat("dd/MM/yyyy")

                with(holder) {
                    setListener(entrenamiento)
                    binding.tvName.text = entrenamiento.nombre
                    binding.fecha.text = sdf.format(entrenamiento.fecha)
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
        mLayoutManager = GridLayoutManager(context, 1)

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

    private fun deleteEntrenamiento(training: Training) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Borrar el siguiente entrenamiento?")
            .setNeutralButton("cancelar") { dialog, which ->
                // nada
            }
            .setPositiveButton("ok") { dialog, which ->
                mGruposRef.child(grupoSeleccionado.currentGroup.value!!).child("Entrenamientos").child(training.id)
                    .removeValue()
            }

            .show()

    }

    //todo al hacer el click largo podamos modificar al atleta
    inner class TrainingHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemEntrenamientoBinding.bind(view)
        val navigation = Navigation.findNavController(mBinding.root)

        fun setListener(training: Training) {
            with(binding.root) {
                setOnClickListener {
                    selectedTraining.selectedTraining = training
                    navigation.navigate(R.id.action_trainingFragment_to_trainingDetailsFragment)
                }
                setOnLongClickListener {
                    if (UserType.type == "Entrenador") {
                        deleteEntrenamiento(training)
                    }
                    true
                }
            }

        }
    }

    /*private fun setExample() {
        Toast.makeText(this.context, grupoSeleccionado.currentGroup.value!!, Toast.LENGTH_SHORT)
            .show()
        FireBaseReferencies.mGruposRef.child(grupoSeleccionado.currentGroup.value!!)
            .child("Formacion")
            .child("ciuaqluier7").setValue(
                Entrenamiento(
                    urlEntrenamiento = "https://www.efdeportes.com/efd142/planificacion-anual-para-entrenamiento-02.jpg",
                    fecha = Date()
                )
            )
        FireBaseReferencies.mGruposRef.child(grupoSeleccionado.currentGroup.value!!)
            .child("Formacion")
            .child("ciuaqluier8").setValue(
                Entrenamiento(
                    urlEntrenamiento = "https://www.monografias.com/docs114/plan-entrenamiento-escrito-del-equipo-medio-fondo/image013.jpg",
                    fecha = Date()
                )
            )
        FireBaseReferencies.mGruposRef.child("Formacion")
            .child("Entrenamientos")
            .child("ciuaqluier9").setValue(
                Entrenamiento(
                    urlEntrenamiento = "https://image.slidesharecdn.com/cfakepathplanificaciondelentrenamientodeportivo-100503094337-phpapp01/95/slide-3-1024.jpg",
                    fecha = Date()            )
            )
        FireBaseReferencies.mGruposRef.child("Formacion")
            .child("Entrenamientos")
            .child("ciuaqluier10").setValue(
                Entrenamiento(
                    urlEntrenamiento = "https://www.monografias.com/trabajos96/estructura-y-planificacion-del-entrenamiento-deportivo/img6.png",
                    fecha = Date()            )
            )
    }
*/

}
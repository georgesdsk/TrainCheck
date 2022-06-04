package com.finde.android.traincheck.Fragments.asist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.ViewModel.AtletaSeleccionado
import com.finde.android.traincheck.databinding.FragmentAsistBinding
import com.finde.android.traincheck.databinding.ItemAtletaBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.finde.android.traincheck.HomeAux
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import java.text.SimpleDateFormat


class AsistFragment : Fragment(), HomeAux {

    private lateinit var mBinding: FragmentAsistBinding
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Athlet, AtletaHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val vmAtleta: AtletaSeleccionado by activityViewModels()
    private val grupoSeleccionado: GrupoSeleccionado by activityViewModels()
    private val format = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAsistBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        setListener()
    }
//todo
    private fun setListener(){

        grupoSeleccionado.currentGroup.observe(viewLifecycleOwner, {
            Toast.makeText(getActivity(),grupoSeleccionado.currentGroup.value, Toast.LENGTH_SHORT  )
            val grupo  = grupoSeleccionado.currentGroup.value!!
            val options =
                FirebaseRecyclerOptions.Builder<Athlet>().setQuery(FireBaseReferencies.mAtletasRef.orderByChild("group").equalTo(grupo), Athlet::class.java)
                    .build()
            mFirebaseAdapter.updateOptions(options)
            //mFirebaseAdapter.notifyDataSetChanged()
        })
    }

    private fun setupAdapter() {
        val grupo  = grupoSeleccionado.currentGroup.value!!
        val options =
            FirebaseRecyclerOptions.Builder<Athlet>().setQuery(FireBaseReferencies.mAtletasRef.orderByChild("group").equalTo(grupo), Athlet::class.java)
                .build()




        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Athlet, AtletaHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_atleta, parent, false)
                return AtletaHolder(view)
            }


            override fun onBindViewHolder(holder: AtletaHolder, position: Int, model: Athlet) {
                val atleta = getItem(position)
                var miss: Boolean = false
                //todo hacer la consulta mas queña
                if( model.listAbsence.containsKey(format.format(Date()))) {
                    miss = true
                }

                with(holder) {
                    setListener(atleta)
                    val completo = atleta.name + " " + atleta.surname
                    binding.cbFalta.isChecked = miss
                    binding.name.text = completo
                    Glide.with(mContext)
                        .load(atleta.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imgAtleta)
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

    override fun goToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

    private fun deleteAtleta(athlet: Athlet) {
        FireBaseReferencies.mAtletasRef.child(athlet.id).removeValue()
    }

    //FirebaseAuth.getInstance().currentUser!!.uid

    //todo hacer que la falta salga siempre con un dia en especia
    private fun setFalta(athlet: Athlet, checked: Boolean) {


        if (checked) {
            FireBaseReferencies.mAtletasRef.child(athlet.id).child("listAbsence").child(format.format(Date())).setValue(Date())

        } else {
            FireBaseReferencies.mAtletasRef.child(athlet.id).child("listAbsence")
                .child(format.format(Date())).removeValue()

        }
    }

    //todo al hacer el click largo podamos modificar al atleta
    inner class AtletaHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemAtletaBinding.bind(view)
        val navigation = Navigation.findNavController(mBinding.root)

        fun setListener(athlet: Athlet) {
            with(binding.root) {
                setOnClickListener {
                    Toast.makeText(context, athlet.id, Toast.LENGTH_SHORT).show()
                    vmAtleta.athletSeleccionado = athlet
                    navigation.navigate(R.id.actionAthletDetails)
                }
                setOnLongClickListener {
                    deleteAtleta(athlet)
                    true
                }
            }

            binding.cbFalta.setOnCheckedChangeListener { compoundButton, checked ->
                setFalta(athlet, checked)
            }
        }
    }
}
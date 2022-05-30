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
import com.finde.android.traincheck.Entities.Atleta
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
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado


class AsistFragment : Fragment(), HomeAux {

    private lateinit var mBinding: FragmentAsistBinding
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Atleta, AtletaHolder>
    private lateinit var mAthletsRef: DatabaseReference
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val vmAtleta: AtletaSeleccionado by activityViewModels()
    private val grupoSeleccionado: GrupoSeleccionado by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAsistBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirebase()
        setAthlets()
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
                FirebaseRecyclerOptions.Builder<Atleta>().setQuery(mAthletsRef.child(grupo), Atleta::class.java)
                    .build()
            mFirebaseAdapter.updateOptions(options)
            //mFirebaseAdapter.notifyDataSetChanged()
        })
    }


    //como recibir el nombre del objeto al que pertenece a lo que hemos pulsado

    private fun setAthlets() {
        var atleta = Atleta(
            name = "Juan",
            surname = "Fernandez Pastor",
            id = "ciuaqluier",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://definicionde.es/wp-content/uploads/2019/04/definicion-de-persona-min.jpg"
        )

        var atleta2 = Atleta(
            name = "Juan alberto",
            surname = "Jimenez franco",
            id = "ciuaqluier2",
            dateBirth = Date(2002, 9, 10),
            photoUrl = "https://iteragrow.com/wp-content/uploads/2018/04/buyer-persona-e1545248524290.jpg"
        )
        mAthletsRef.child("Formacion").child("ciuaqluier1").setValue(atleta2)
        mAthletsRef.child("Altorendimiento").child("ciuaqluier2").setValue(atleta)
    }

    private fun setupAdapter() {
        val grupo  = grupoSeleccionado.currentGroup.value!!
        val options =
            FirebaseRecyclerOptions.Builder<Atleta>().setQuery(mAthletsRef.child(grupo), Atleta::class.java)
                .build()


        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Atleta, AtletaHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_atleta, parent, false)
                return AtletaHolder(view)
            }


            override fun onBindViewHolder(holder: AtletaHolder, position: Int, model: Atleta) {
                val atleta = getItem(position)

                with(holder) {
                    setListener(atleta)
                    val completo = atleta.name + " " + atleta.surname
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

    private fun setupFirebase() {
        mAthletsRef =
            FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app")
                .reference.child("atleta")
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

    private fun deleteAtleta(atleta: Atleta) {
        mAthletsRef.child(atleta.id).removeValue()
    }

    //FirebaseAuth.getInstance().currentUser!!.uid

    //todo hacer que la falta salga siempre con un dia en especia
    private fun setFalta(atleta: Atleta, checked: Boolean) {

        if (checked) {
            /*mAthletsRef.child(atleta.id).child("listaFaltas")
                .updateChildren(Date(2019,10,1))*/
        } else {
            mAthletsRef.child(atleta.id).child("listaFaltas")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
        }
    }

    //todo al hacer el click largo podamos modificar al atleta
    inner class AtletaHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemAtletaBinding.bind(view)
        val navigation = Navigation.findNavController(mBinding.root)

        fun setListener(atleta: Atleta) {
            with(binding.root) {
                setOnClickListener {
                    Toast.makeText(context, atleta.id, Toast.LENGTH_SHORT).show()
                    vmAtleta.atletaSeleccionado = atleta
                    navigation.navigate(R.id.actionAthletDetails)
                }
                setOnLongClickListener {
                    deleteAtleta(atleta)
                    true
                }
            }

            binding.cbFalta.setOnCheckedChangeListener { compoundButton, checked ->
                setFalta(atleta, checked)
            }
        }
    }
}
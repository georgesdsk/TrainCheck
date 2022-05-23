package com.finde.android.traincheck

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.finde.android.traincheck.Entities.Atleta
import com.finde.android.traincheck.Entities.Entrenamiento
import com.finde.android.traincheck.databinding.FragmentAsistBinding
import com.finde.android.traincheck.databinding.ItemAtletaBinding

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AsistFragment : Fragment(), HomeAux {

    private lateinit var mBinding: FragmentAsistBinding
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Atleta, AtletaHolder>

    // private lateinit var mGridLayout: GridLayoutManager
    //private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var mAthletsRef: DatabaseReference
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

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

    }

    private fun setAthlets() {
        var atleta = Atleta(nombre = "YO", apellidos = "Tu", id = "2", photoUrl = "https://lh3.google.com/u/0/ogw/ADea4I5WlHogjAsdRdJmumQWcb9teMIzCSVQ-9WvOFNc=s32-c-mo" )
        mAthletsRef.child("atleta").child("ciuaqluier").setValue(atleta)
    }

    private fun setupAdapter() {

        val query = mAthletsRef

        val options =
            FirebaseRecyclerOptions.Builder<Atleta>().setQuery(query, Atleta::class.java).build()


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

                    binding.tvName.text = atleta.nombre
                    binding.cbFalta.text = atleta.id//atleta.listaFaltas.size.toString()


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

    private fun setupFirebase() {
        mAthletsRef =
            FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app/")
                .reference.child("atleta")
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
        val databaseReference = FirebaseDatabase.getInstance().reference.child("atleta")
        databaseReference.child(atleta.id).removeValue()
    }

    //todo hacer que la falta salga siempre con un dia en especia
    private fun setFalta(atleta: Atleta, checked: Boolean) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("atleta")
        if (checked) {
            databaseReference.child(atleta.id).child("listaFaltas")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
        } else {
            databaseReference.child(atleta.id).child("listaFaltas")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
        }
    }

    //todo al hacer el click largo podamos modificar al atleta
    inner class AtletaHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val binding = ItemAtletaBinding.bind(view)
        val binding = ItemAtletaBinding.bind(view)

        fun setListener(atleta: Atleta) {
            //binding.btnDelete.setOnClickListener { deleteAtleta(atleta) }

            binding.cbFalta.setOnCheckedChangeListener { compoundButton, checked ->
                setFalta(atleta, checked)
            }
        }
    }
}
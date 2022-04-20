package com.cursosant.android.snapshots

import android.content.Context
import android.os.Bundle
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
import com.cursosant.android.snapshots.Entities.Atleta
import com.cursosant.android.snapshots.databinding.FragmentAsistBinding
import com.cursosant.android.snapshots.databinding.ItemAtletaBinding

import com.cursosant.android.snapshots.databinding.ItemSnapshotBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.type.Date

class AsistFragment : Fragment() , HomeAux {

    private lateinit var mBinding: FragmentAsistBinding

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Atleta, AtletaHolder>
   // private lateinit var mGridLayout: GridLayoutManager
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAsistBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseDatabase.getInstance().reference.child("atleta")
/*
        val options =
        FirebaseRecyclerOptions.Builder<Atleta>().setQuery(query, {
            val atleta = it.getValue(Atleta::class.java)
            atleta!!.id = it.key!!
            atleta
        }).build()
        */
        val options = FirebaseRecyclerOptions.Builder<Atleta>()
            .setQuery(query, Atleta::class.java).build()


        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Atleta, AtletaHolder>(options){
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_atleta, parent, false)
                return AtletaHolder(view)
            }

            override fun onBindViewHolder(holder: AtletaHolder, position: Int, model: Atleta) {
                val atleta = getItem(position)

                with(holder){
                    setListener(atleta)

                    binding.tvName.text = atleta.nombre + " "+atleta.apellidos
                    binding.cbFalta.text = atleta.id//atleta.listaFaltas.size.toString()
                  /*
                    FirebaseAuth.getInstance().currentUser?.let {
                        binding.cbFalta.isChecked = true//atleta.listaFaltas.contains(Date.getDefaultInstance())
                            /*
                            atleta.likeList
                                .containsKey(it.uid)
                                */
                    }
                    */

                    Glide.with(mContext)
                        .load(atleta.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imgAtleta)
                }
            }

            override fun onDataChanged() {
                super.onDataChanged()
                mBinding.progressBar.visibility = View.GONE
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }

     //   mGridLayout = GridLayoutManager(context, 2)

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            mLayoutManager = LinearLayoutManager(context)
            //layoutManager = mGridLayout
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

    private fun deleteAtleta(atleta: Atleta){
        val databaseReference = FirebaseDatabase.getInstance().reference.child("atletas")
        databaseReference.child(atleta.id).removeValue()
    }
//todo hacer que la falta salga siempre con un dia en especia
    private fun setFalta(atleta: Atleta, checked: Boolean){
        val databaseReference = FirebaseDatabase.getInstance().reference.child("atletas")
        if (checked){
            databaseReference.child(atleta.id).child("listaFaltas")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
        } else {
            databaseReference.child(atleta.id).child("listaFaltas")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
        }
    }

    //todo al hacer el click largo podamos modificar al atleta
    inner class AtletaHolder(view: View) : RecyclerView.ViewHolder(view){
        //val binding = ItemAtletaBinding.bind(view)
        val binding = ItemAtletaBinding.bind(view)

        fun setListener(atleta: Atleta){
            //binding.btnDelete.setOnClickListener { deleteAtleta(atleta) }

            binding.cbFalta.setOnCheckedChangeListener { compoundButton, checked ->
                setFalta(atleta, checked)
            }
        }
    }
}
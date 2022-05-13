package com.finde.android.traincheck

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.finde.android.traincheck.databinding.FragmentTrainingBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class TrainingFragment : Fragment() {

    private val RC_GELLERY = 18
    private val PATH_TRAININGS = "trainings"

    private lateinit var mBinding: FragmentTrainingBinding
    private lateinit var mStorageReference: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mFragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTrainingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.fab.setOnClickListener { postTraining() }

        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(PATH_TRAININGS)
    }

    private fun postTraining() {

        val fragment = AddFragment()
        val fragmentManager = getParentFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack("cesta") // todo se anade para el paso atras
        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.commit()

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GELLERY)
    }

    override fun onPause() {
        super.onPause()
        parentFragment?.fragmentManager?.fragments?.last()?.onDestroy()
    }



}
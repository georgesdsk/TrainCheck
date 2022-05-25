package com.finde.android.traincheck.Fragments.training

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.finde.android.traincheck.Entities.Entrenamiento
import com.finde.android.traincheck.R
import com.finde.android.traincheck.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddFragment : Fragment() {

    private val RC_GELLERY = 18
    private val PATH_ENTRENAMIENTOS = "entrenamientos"

    private lateinit var mBinding: FragmentAddBinding
    private lateinit var mStorageReference: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference

    private var mPhotoSelectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAddBinding.inflate(inflater, container, false)
      /*  mBinding.tvMessage.setOnClickListener {
            Navigation.findNavController(mBinding.root).navigate(R.id.navigateToAdd)

        }
        */

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnPost.setOnClickListener { subirEntrenamiento() }

        mBinding.btnSelect.setOnClickListener { openGallery() }

        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(PATH_ENTRENAMIENTOS)
        val mAuth = FirebaseAuth.getInstance()


    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GELLERY)
    }

    private fun subirEntrenamiento() {
        mBinding.progressBar.visibility = View.VISIBLE
        val key = mDatabaseReference.push().key!!
        val storageReference = mStorageReference.child(PATH_ENTRENAMIENTOS).child("my_training2")

        //.child(FirebaseAuth.getInstance().currentUser!!.uid).child(key)

        if (mPhotoSelectedUri != null) {
            storageReference.putFile(mPhotoSelectedUri!!)
                    .addOnProgressListener {
                        val progress = (100 * it.bytesTransferred/it.totalByteCount).toDouble()
                        mBinding.progressBar.progress = progress.toInt()
                        mBinding.tvMessage.text = "$progress%"
                    }
                    .addOnCompleteListener{
                        mBinding.progressBar.visibility = View.INVISIBLE
                    }
                    .addOnSuccessListener {
                        Snackbar.make(mBinding.root, "Entrenamiento publicado.",
                                Snackbar.LENGTH_SHORT).show()
                        it.storage.downloadUrl.addOnSuccessListener {
                            guardarEntrenamiento(key, it.toString(), mBinding.etTitle.text.toString().trim())
                            mBinding.tilTitle.visibility = View.GONE
                            mBinding.tvMessage.text = getString(R.string.post_entrenamiento)
                        }
                    }
                    .addOnFailureListener{
                        Snackbar.make(mBinding.root, "No se pudo subir, intente más tarde.",
                                Snackbar.LENGTH_SHORT).show()
                    }
        }
    }

    private fun guardarEntrenamiento(key: String, url: String, title: String){
        val entrenamiento = Entrenamiento( id = key, urlEntrenamiento= url, nombre = title)// fecha = Date.getDefaultInstance(),
        mDatabaseReference.child(key).setValue(entrenamiento)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == RC_GELLERY){
                mPhotoSelectedUri = data?.data
                mBinding.imgPhoto.setImageURI(mPhotoSelectedUri)
                mBinding.tilTitle.visibility = View.VISIBLE
                mBinding.tvMessage.text = getString(R.string.post_message_valid_title)
            }
        }
    }
}
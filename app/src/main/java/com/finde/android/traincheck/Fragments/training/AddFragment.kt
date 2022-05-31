package com.finde.android.traincheck.Fragments.training

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.finde.android.traincheck.Entities.Entrenamiento
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat.getDateInstance
import java.util.*

class AddFragment : Fragment() {

    private val RC_GELLERY = 18

    private lateinit var mBinding: FragmentAddBinding
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

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GELLERY)
    }

    private fun subirEntrenamiento() {
        mBinding.progressBar.visibility = View.VISIBLE
        var selectGroup : String

        //mejorar
        if (mBinding.rbGroups.checkedRadioButtonId==R.id.alto_rendimiento){
            selectGroup = "AltoRendimiento"
        }
        else{
            selectGroup = "Formacion"

        }

        val key = FireBaseReferencies.mDatabaseRef.child("Grupos").child(selectGroup).child("Entrenamientos").push().key!!
        val storageReference = FireBaseReferencies.mStorageRef.child("Entrenamientos").child(key)

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
                            guardarEntrenamiento(key, it.toString(), mBinding.etTitle.text.toString().trim(), selectGroup)
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

    private fun guardarEntrenamiento(key: String, url: String, title: String, selectGroup: String){
        val entrenamiento = Entrenamiento( id = key, urlEntrenamiento = url, nombre = title, group = selectGroup, fecha = Date())// ,
        FireBaseReferencies.mDatabaseRef.child("Grupos").child(selectGroup).child("Entrenamientos").child(key).setValue(entrenamiento)

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
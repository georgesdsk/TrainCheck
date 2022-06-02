package com.finde.android.traincheck.register

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.Fragments.main_page.MainActivity
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mFirebaseAuth
import com.finde.android.traincheck.ViewModel.FireBaseReferencies.Companion.mGruposRef
import com.finde.android.traincheck.ViewModel.RegistrationViewModel
import com.finde.android.traincheck.databinding.FragmentRegister2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


//async
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.google.android.material.snackbar.Snackbar

//import com.finde.android.traincheck.register.Register2.Callback as Callback1

class Register2 : Fragment() {

    private lateinit var mBinding: FragmentRegister2Binding
    private val registrationViewModel: RegistrationViewModel by activityViewModels()
    private val RC_GELLERY = 18
    private var mPhotoSelectedUri: Uri? = null

    private var key: String? = null

    //todo rcgallery

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentRegister2Binding.inflate(inflater, container, false)
        //checkInputText()

        return mBinding.root
    }

    private fun registAthlet() {

        var password = registrationViewModel.password.value
        var password2 = registrationViewModel.password2.value

        val atleta = Athlet(
            name = registrationViewModel.name.value.toString(),
            surname = registrationViewModel.surname.value.toString(),
            group = registrationViewModel.nGroup.value.toString(),
            mail = registrationViewModel.mail.value.toString(),
            photoUrl = key.toString()
        )
        checkAthlet(atleta, password, password2)

    }

    private fun checkAthlet(athlet: Athlet, password: String?, password2: String?)  {
        var check = false
        if (athlet.name.isEmpty() || athlet.surname.isEmpty() ||
            athlet.group.isEmpty() || athlet.mail.isEmpty() ||
            password!!.isEmpty() || password2!!.isEmpty()
        ) {
            Toast.makeText(context, "All data is required", Toast.LENGTH_SHORT).show()
        } else {
            if (password == password2) { // check the group
                if (checkMail(athlet.mail)) {
                    checkGroup(athlet, password)
                }
            } else {
                Toast.makeText(
                    requireActivity(), "Passwords aren`t the same",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // muy acoplado, pero no deja esperar hasta un onDataChange, asi que el codigo sigue y no deja comprobar
    // si el grupo existe o no
    private fun checkGroup(athlet: Athlet, password: String) {

        val gruposListener = object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                var exists: Boolean = false
                if (snapshots.exists()) {
                    var grupos = snapshots.children
                    grupos.find { grupo ->
                        if (grupo.key == athlet.group) {
                            exists = true
                            createUser(athlet, password)
                        }
                        exists
                    }
                    if (!exists) {
                        Toast.makeText(
                            requireActivity(), "Group doesn`t exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        mGruposRef.addListenerForSingleValueEvent(gruposListener) // callbackm // se setea el listener a los grupos
    }


    private fun createUser(athlet: Athlet, password: String) {
        mFirebaseAuth.createUserWithEmailAndPassword(athlet.mail, password)
            .addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                createUserOnDatabase(athlet, mFirebaseAuth.currentUser!!.uid)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    requireActivity(), "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createUserOnDatabase(athlet: Athlet, uid: String) {
        mGruposRef.child(athlet.group).child("Atletas").child(uid).setValue(athlet).addOnSuccessListener {
            Toast.makeText(requireActivity(), "User created", Toast.LENGTH_SHORT).show()
            reload()
        }

    }

    private fun checkMail(mail: String): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnclickListeners()
        mBinding.btnSelect.setOnClickListener { openGallery() }
        mBinding.btnPost.setOnClickListener { subirFoto() }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GELLERY)
    }


    //todo generalizarlo con el metodo de subida de imagen
    private fun subirFoto() {
        mBinding.progressBar.visibility = View.VISIBLE

        key = FireBaseReferencies.mDatabaseRef.child("Fotos").push().key!!
        val storageReference = FireBaseReferencies.mStorageRef.child("Fotos").child(key.toString())

        if (mPhotoSelectedUri != null) {
            storageReference.putFile(mPhotoSelectedUri!!)
                .addOnProgressListener {
                    val progress = (100 * it.bytesTransferred/it.totalByteCount).toDouble()
                    mBinding.progressBar.progress = progress.toInt()
                    mBinding.progreso.text = "$progress%"
                }
                .addOnCompleteListener{
                    mBinding.progressBar.visibility = View.INVISIBLE
                }
                .addOnSuccessListener {
                    Snackbar.make(mBinding.root, "Foto publicado.",
                        Snackbar.LENGTH_SHORT).show()
                    it.storage.downloadUrl.addOnSuccessListener {
                        //guardarFoto(key, it.toString(), mBinding.etTitle.text.toString().trim(), selectGroup)
                        mBinding.progreso.text ="Perfecto"
                    }
                }
                .addOnFailureListener{
                    Snackbar.make(mBinding.root, "No se pudo subir, intente más tarde.",
                        Snackbar.LENGTH_SHORT).show()
                }
        }
    }

  /*  private fun guardarFoto(key: String, url: String, title: String, selectGroup: String){
        FireBaseReferencies.mDatabaseRef.child("Grupos").child(selectGroup).child("Fotos").child(key).setValue(Foto)
    }*/



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == RC_GELLERY){
                mPhotoSelectedUri = data?.data
                mBinding.imgPhoto.setImageURI(mPhotoSelectedUri)
            }
        }
    }


    private fun reload() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        this.startActivity(intent)
    }

    private fun setupOnclickListeners() {
        val navigation = Navigation.findNavController(mBinding.root)
        mBinding.backButton.setOnClickListener { navigation.navigate(R.id.action_register2_to_register1) }
        mBinding.buttonNext.setOnClickListener {
            if(!key.isNullOrBlank()){
                registAthlet()
            }else{
                Snackbar.make(mBinding.root, "Suba una foro anteriormente",
                    Snackbar.LENGTH_SHORT).show()
            }

         }

    }


}


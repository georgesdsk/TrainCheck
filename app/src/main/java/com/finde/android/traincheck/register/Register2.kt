package com.finde.android.traincheck.register

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.finde.android.traincheck.Entities.Atleta
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
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//import com.finde.android.traincheck.register.Register2.Callback as Callback1

class Register2 : Fragment() {

    private lateinit var mBinding: FragmentRegister2Binding
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

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
        /*  var name = registrationViewModel.name.value
          var surname = registrationViewModel.surname.value
          var nGroup = registrationViewModel.nGroup.value
          var mail = registrationViewModel.mail.value*/

        var password = registrationViewModel.password.value
        var password2 = registrationViewModel.password2.value

        val atleta = Atleta(
            name = registrationViewModel.name.value.toString(),
            surname = registrationViewModel.surname.value.toString(),
            group = registrationViewModel.nGroup.value.toString(),
            mail = registrationViewModel.mail.value.toString()
        )
        checkAthlet(atleta, password, password2)

        /*        if (checkAthlet(atleta, password, password2)) {
                    createUser(atleta, password!!)
                 }
                 checkGroup(Atleta(group = "Formación"), "123456")
          //createUser(Atleta(group = "Formación"), )*/


    }

    private fun checkAthlet(atleta: Atleta, password: String?, password2: String?): Boolean {
        var check = false
        if (atleta.name.isEmpty() || atleta.surname.isEmpty() ||
            atleta.group.isEmpty() || atleta.mail.isEmpty() ||
            password!!.isEmpty() || password2!!.isEmpty()
        ) {
            Toast.makeText(context, "All data is required", Toast.LENGTH_SHORT).show()
        } else {
            if (password == password2) { // check the group
                if (checkMail(atleta.mail)) {
                    checkGroup(atleta, password)
                }
            } else {
                Toast.makeText(
                    requireActivity(), "Passwords aren`t the same",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return check;
    }

    // muy acoplado, pero no deja esperar hasta un onDataChange, asi que el codigo sigue y no deja comprobar
    // si el grupo existe o no
    private fun checkGroup(atleta: Atleta, password: String) {

        val gruposListener = object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                var exists: Boolean = false
                if (snapshots.exists()) {
                    var grupos = snapshots.children
                    grupos.find { grupo ->
                        if (grupo.key == atleta.group) {
                            exists = true
                            createUser(atleta, password)
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


    private fun createUser(atleta: Atleta, password: String) {
        mFirebaseAuth.createUserWithEmailAndPassword(atleta.mail, password)
            .addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                createUserOnDatabase(atleta, mFirebaseAuth.currentUser!!.uid)
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

    private fun createUserOnDatabase(atleta: Atleta, uid: String) {
        mGruposRef.child(atleta.group).child(uid).setValue(atleta).addOnSuccessListener {
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
    }

    private fun reload() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        this.startActivity(intent)
    }

    private fun setupOnclickListeners() {
        val navigation = Navigation.findNavController(mBinding.root)
        mBinding.backButton.setOnClickListener { navigation.navigate(R.id.action_register2_to_register1) }
        mBinding.buttonNext.setOnClickListener { registAthlet() }
    }


}


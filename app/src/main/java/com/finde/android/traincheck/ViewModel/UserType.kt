package com.finde.android.traincheck.ViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserType {
    companion object{
        lateinit var type: String

        fun create(){
            type = "Atleta"
        }
    }
}
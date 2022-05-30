package com.finde.android.traincheck.ViewModel


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireBaseReferencies() {
    companion object{
        lateinit var mDatabaseRef: DatabaseReference
        lateinit var mGruposRef: DatabaseReference
        lateinit var mFormacionRef: DatabaseReference
        lateinit var mAltoRendimientoRef: DatabaseReference
        lateinit var mEntrenadoresRef: DatabaseReference
        lateinit var mStorageRef: StorageReference

        lateinit var mFirebaseAuth: FirebaseAuth

        fun create(){
            mFirebaseAuth = FirebaseAuth.getInstance()
            mDatabaseRef =  FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app").reference
            mEntrenadoresRef = mDatabaseRef.child("Entrenadores")
            mGruposRef = mDatabaseRef.child("Grupos")
            mFormacionRef = mDatabaseRef.child("Grupos").child("Formacion")
            mAltoRendimientoRef = mDatabaseRef.child("Grupos").child("AltoRendimiento")
            mStorageRef = FirebaseStorage.getInstance().reference
        }



    }


}
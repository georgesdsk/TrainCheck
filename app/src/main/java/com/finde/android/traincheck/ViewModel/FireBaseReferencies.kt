package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FireBaseReferencies() {
    companion object{
        lateinit var mDatabaseRef: DatabaseReference
        lateinit var mFormacionRef: DatabaseReference
        lateinit var mAltoRendimientoRef: DatabaseReference
        lateinit var mEntrenadoresRef: DatabaseReference
        lateinit var mFirebaseAuth: FirebaseAuth

        fun create(){
            mFirebaseAuth = FirebaseAuth.getInstance()
            mDatabaseRef =  FirebaseDatabase.getInstance("https://traincheck-481b2-default-rtdb.europe-west1.firebasedatabase.app").reference
            mEntrenadoresRef = mDatabaseRef.child("Entrenadores")
            mFormacionRef = mDatabaseRef.child("Grupos").child("Formacion")
            mAltoRendimientoRef = mDatabaseRef.child("Grupos").child("AltoRendimiento")
        }



    }


}
package com.finde.android.traincheck.Entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import com.google.type.Date
import java.text.DateFormat

@IgnoreExtraProperties
data class Entrenamiento(
    @get:Exclude var id: String = "",
    var urlEntrenamiento: String = "",
    var nombre: String = "",
    var group: String = "",
    val fecha: java.util.Date = java.util.Date()
)



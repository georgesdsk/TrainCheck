package com.finde.android.traincheck.Entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import com.google.type.Date

@IgnoreExtraProperties
data class Entrenamiento(@get:Exclude var id: String = "",
                        // var fecha: Date = Date.getDefaultInstance(),
                         var urlEntrenamiento: String = "",
                         var nombre: String ="",
                         var group: String =""
                         )



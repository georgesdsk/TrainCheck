package com.cursosant.android.snapshots.Entities

import com.google.firebase.database.Exclude
import com.google.type.Date

data class Entrenamiento(@get:Exclude var id: String = "",
                         var fecha: Date = Date.getDefaultInstance(),
                         var urlEntrenamiento: String
                         )
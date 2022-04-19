package com.cursosant.android.snapshots

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Atleta(@get:Exclude var id: String = "",
                  var nombre: String = "",
                  var apellidos: String = "",
                  var photoUrl: String ="",
                  var likeList: Map<String, Boolean> = mutableMapOf())

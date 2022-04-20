package com.cursosant.android.snapshots.Entities

import com.google.firebase.database.Exclude

data class Group (@get:Exclude var id: String = "",
                  var name: String ="",
                  var listaAtletas: List<Atleta>,
                  var listaEntrenamientos: List<Entrenamiento>
                  )
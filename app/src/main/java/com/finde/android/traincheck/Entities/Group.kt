package com.cursosant.android.traincheck.Entities

import com.finde.android.traincheck.Entities.Atleta
import com.finde.android.traincheck.Entities.Entrenamiento
import com.google.firebase.database.Exclude

data class Group (@get:Exclude var id: String = "",
                  var name: String ="",
                  var listaAtletas: List<Atleta>,
                  var listaEntrenamientos: List<Entrenamiento>
                  )
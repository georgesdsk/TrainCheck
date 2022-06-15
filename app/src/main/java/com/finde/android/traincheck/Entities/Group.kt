package com.cursosant.android.traincheck.Entities

import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.Entities.Training
import com.google.firebase.database.Exclude

data class Group (@get:Exclude var id: String = "",
                  var name: String ="",
                  var listaAthlets: List<Athlet> = listOf(),
                  var listaTrainings: List<Training> = listOf()
                  )
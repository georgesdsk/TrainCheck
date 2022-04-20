package com.cursosant.android.snapshots.Entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.type.Date



@IgnoreExtraProperties
data class Atleta( var id: String = "",
                  var nombre: String = "",
                  var photoUrl: String ="",
                  var apellidos: String = ""
                  /*,
                  var fechaNacimiento: Date = Date.getDefaultInstance(),
                  var tipoAsistencia: Int = 1, // de tres tipo
                  var listaFaltas: List<Date>,
                  var listaStats: List<Stat>,
                  var listaMolestias: List<Injury>*/
)

package com.finde.android.traincheck.Entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Atleta(@get:Exclude var id: String = "",
                  var nombre: String = "",
                  var apellidos: String = "",
                  var photoUrl: String =""
    /*,
    var fechaNacimiento: Date = Date.getDefaultInstance(),
    var tipoAsistencia: Int = 1, // de tres tipo
    var listaFaltas: List<Date>,
    var listaStats: List<Stat>,
    var listaMolestias: List<Injury>*/
)

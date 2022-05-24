package com.finde.android.traincheck.Entities

import com.cursosant.android.traincheck.Entities.Injury
import com.cursosant.android.traincheck.Entities.Stat
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@IgnoreExtraProperties
data class Atleta(@get:Exclude var id: String = "",
                  var nombre: String = "",
                  var apellidos: String = "",
                  var grupo: String ="",
                  var photoUrl: String ="",
                  var fechaNacimiento: Date = Date(2019,8,10),
                  var tipoAsistencia: Int = 1, // de tres tipo
                  var listaFaltas: List<Date> = mutableListOf(),
                  var listaStats: List<Stat> = mutableListOf(),
                  var listaMolestias: List<Injury> = mutableListOf()
)

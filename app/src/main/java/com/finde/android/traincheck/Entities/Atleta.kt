package com.finde.android.traincheck.Entities

import com.cursosant.android.traincheck.Entities.Injury
import com.cursosant.android.traincheck.Entities.Stat
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@IgnoreExtraProperties
data class Atleta(@get:Exclude var id   : String = "",
                  var name: String = "",
                  var surname: String = "",
                  var group: String ="",
                  var mail: String = "",
                  var photoUrl: String ="",
                  var dateBirth: Date = Date(2019,8,10),
                  var asistType: Int = 1, // de tres tipo
                  var listAbsence: List<Date> = mutableListOf(),
                  var listStats: List<Stat> = mutableListOf(),
                  var injuryList: List<Injury> = mutableListOf()
)

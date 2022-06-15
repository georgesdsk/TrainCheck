package com.finde.android.traincheck.Entities

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@IgnoreExtraProperties
data class Athlet( var id   : String = "",
                  var name: String = "",
                  var surname: String = "",
                  var group: String ="Formacion",
                  var mail: String = "a@a.com",
                  var photoUrl: String ="",
                  var dateBirth: Date = Date(2019,8,10),
                  var asistType: Int = 1, // de tres tipo
                  var listAbsence: Map<String, Date> = mapOf(),
                  var listStats: Map<String, List<Int>> = mapOf(),
                  var injuryList: Map<String, Date> = mapOf()
)

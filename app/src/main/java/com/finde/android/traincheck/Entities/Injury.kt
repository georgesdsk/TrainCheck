package com.cursosant.android.traincheck.Entities

import com.google.firebase.database.Exclude
import com.google.type.Date

data class Injury(@get:Exclude var id: String = "", var date: Date, var active: Boolean, var description: String)
package com.finde.android.traincheck.Entities

import com.google.firebase.database.Exclude

data class trainerRegister( @get:Exclude var id: String = "",
                            var nombre: String = "",
                            var apellidos: String = "",
                            var correo: String = "",
                            var contrasenia: String = "",
                            var codigo: String = "") {
}
package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finde.android.traincheck.Entities.Athlet

class TriggerDatosCargados : ViewModel() {
     var datosCargados: MutableLiveData <Boolean> = MutableLiveData()
}
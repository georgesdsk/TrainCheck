package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finde.android.traincheck.Entities.Entrenamiento
import java.util.HashMap

class VmEstadisticas: ViewModel() {
    lateinit var name: String
    var mapaSumatorio : MutableLiveData<HashMap<String, List<Int>>> = MutableLiveData()
}
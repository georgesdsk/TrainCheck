package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GrupoSeleccionado : ViewModel() {
    val currentGroup: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
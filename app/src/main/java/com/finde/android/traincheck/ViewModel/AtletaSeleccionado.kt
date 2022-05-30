package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finde.android.traincheck.Entities.Atleta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AtletaSeleccionado : ViewModel() {
     var atletaSeleccionado: Atleta = Atleta()


}
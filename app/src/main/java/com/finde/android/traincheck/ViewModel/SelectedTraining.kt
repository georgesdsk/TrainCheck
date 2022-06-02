package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.ViewModel
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.Entities.Entrenamiento

class SelectedTraining : ViewModel() {
     var selectedTraining: Entrenamiento = Entrenamiento()
}
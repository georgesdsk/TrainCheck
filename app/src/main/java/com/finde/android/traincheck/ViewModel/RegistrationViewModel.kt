package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RegistrationViewModel : ViewModel() {
     lateinit var name: MutableLiveData<String>
     lateinit var surname: MutableLiveData<String>
     lateinit var nGroup: MutableLiveData<String>
     lateinit var dateOfBirth: MutableLiveData<String>
     lateinit var mail: MutableLiveData<String>
     lateinit var password: MutableLiveData<String>
     lateinit var password2: MutableLiveData<String>

}
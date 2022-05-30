package com.finde.android.traincheck.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finde.android.traincheck.Entities.UserRegister


class RegistrationViewModel : ViewModel() {
     var name: MutableLiveData<String> = MutableLiveData("")
      var surname: MutableLiveData<String> = MutableLiveData("")
      var nGroup: MutableLiveData<String> = MutableLiveData("")
      var dateOfBirth: MutableLiveData<String> = MutableLiveData("")
      var mail: MutableLiveData<String> = MutableLiveData("")
      var password: MutableLiveData<String> = MutableLiveData("")
      var password2: MutableLiveData<String> = MutableLiveData("")
      var lateinit userRegister: UserRegister

}

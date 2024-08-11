package com.bestdriver.aaa_klaxon.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserViewModel : ViewModel() {
    // 사용자 이름과 자동차 번호를 저장하는 MutableLiveData
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _carNumber = MutableLiveData<String>()
    val carNumber: LiveData<String> get() = _carNumber

    // 사용자 정보를 업데이트하는 메서드
    fun updateUser(name: String, car: String) {
        _userName.value = name
        _carNumber.value = car
    }
}

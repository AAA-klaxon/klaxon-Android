package com.bestdriver.aaa_klaxon.network.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


class SignUpViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _nickname = MutableLiveData("")
    val nickname: LiveData<String> get() = _nickname

    private val _car_number = MutableLiveData("")
    val car_number: LiveData<String> get() = _car_number

    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    private val _dialogMessage = MutableLiveData("")
    val dialogMessage: LiveData<String> get() = _dialogMessage

    // 이메일 형식 검증을 위한 정규식
    private val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")

    fun onSignUpClick(navController: NavController) {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        val nickname = _nickname.value.orEmpty()
        val car_number = _car_number.value.orEmpty()

        if (email.isEmpty() || password.isEmpty() || nickname.isEmpty() || car_number.isEmpty()) {
            _dialogMessage.value = "모든 필드를 올바르게 입력해 주세요."
            _showDialog.value = true
            return
        }

        if (!isEmailValid(email)) {  // 이메일 유효성 검사 추가
            _dialogMessage.value = "올바른 이메일 형식을 입력해 주세요."
            _showDialog.value = true
            return
        }

        viewModelScope.launch {
            try {
                val request = SignUpRequest(email, password, nickname, car_number)
                val response = authApiService.signUp(request)

                if (response.isSuccessful) {
                    response.body()?.let { signUpResponse ->
                        if (signUpResponse.isSuccess) {
                            navController.popBackStack()
                        } else {
                            _dialogMessage.value = "회원가입 실패: ${signUpResponse.message}"
                            _showDialog.value = true
                        }
                    }
                } else {
                    handleErrorResponse(response.code())
                }
            } catch (e: HttpException) {
                _dialogMessage.value = "회원가입에 실패했습니다: ${e.message()}"
                _showDialog.value = true
            } catch (e: Exception) {
                Log.e("SignUp", "Unknown error occurred", e)
                _dialogMessage.value = "알 수 없는 오류가 발생했습니다: ${e.localizedMessage}"
                _showDialog.value = true
            }
        }
    }

    // 이메일 유효성 검증 함수
    private fun isEmailValid(email: String): Boolean {
        return emailPattern.matches(email)
    }

    private fun handleErrorResponse(code: Int) {
        when (code) {
            409 -> {
                _dialogMessage.value = "이미 존재하는 사용자입니다."
            }
            400 -> {
                _dialogMessage.value = "잘못된 요청입니다. 모든 필드를 확인해 주세요."
            }
            500 -> {
                _dialogMessage.value = "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
            }
            else -> {
                _dialogMessage.value = "알 수 없는 오류가 발생했습니다: $code"
            }
        }
        _showDialog.value = true
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun updateNickname(newNickname: String) {
        _nickname.value = newNickname
    }

    fun updateCarNumber(newCarNumber: String) {
        _car_number.value = newCarNumber
    }

    fun updateDialogMessage(message: String) {
        _dialogMessage.value = message
    }

    fun showErrorDialog() {
        _showDialog.value = true
    }

    fun hideErrorDialog() {
        _showDialog.value = false
    }
}

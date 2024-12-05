package com.bestdriver.aaa_klaxon.network.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.bestdriver.aaa_klaxon.network.auth.AuthApiService
import com.bestdriver.aaa_klaxon.network.TokenManager
import com.bestdriver.aaa_klaxon.network.auth.LoginRequest
import retrofit2.Response

class LoginViewModel(
    private val authApiService: AuthApiService,
    private val context: Context // Context를 ViewModel에 전달
) : ViewModel() {

    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _loginError = MutableLiveData("")
    val loginError: LiveData<String> get() = _loginError

    private val tokenManager = TokenManager(context) // TokenManager 인스턴스 생성

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun onLoginClick(onSuccess: (String) -> Unit) {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val response = authApiService.login(LoginRequest(email, password))
                    if (response.isSuccessful) {
                        // 헤더에서 액세스 토큰 추출
                        val accessToken = response.headers()["Authorization"]
                        // 바디에서 리프레시 토큰 추출
                        val refreshToken = response.body()?.result?.refreshToken

                        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                            tokenManager.saveAccessToken(accessToken)
                            tokenManager.saveRefreshToken(refreshToken)
                            onSuccess(accessToken)
                        } else {
                            _loginError.value = "로그인 실패: ${response.code()}"

                        }
                    } else {
                        _loginError.value = "로그인 실패: ${response.code()}"
                    }
                } catch (e: HttpException) {
                    _loginError.value = "서버 오류: ${e.message()}"
                } catch (e: Exception) {
                    _loginError.value = "알 수 없는 오류 발생: ${e.localizedMessage}"
                }
            }
        } else {
            _loginError.value = "아이디와 비밀번호를 입력해 주세요."
        }
    }
}

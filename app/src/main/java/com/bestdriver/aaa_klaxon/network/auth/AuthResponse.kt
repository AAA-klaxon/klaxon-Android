package com.bestdriver.aaa_klaxon.network.auth

data class SignUpResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String
)

data class LoginResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: LoginResult? // 수정: result 안에 리프레시 토큰 포함
)

data class LoginResult(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: RefreshResult? // 수정: result 안에 액세스 토큰 포함
)


data class AuthResult(
    val refreshToken: String
)



data class RefreshResult(
    val refreshToken: String
)

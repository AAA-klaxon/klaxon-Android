package com.bestdriver.aaa_klaxon.network.auth

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val car_number: String,
    val phone_number: String
)

package com.bestdriver.aaa_klaxon.network.home

// 데이터 클래스 정의
data class TrafficError(
    val recognized_sign_name: String,
    val recognized_count: Int,
    val misrecognition_count: Int,
    val misrecognition_rate: String
)

// API 응답 데이터 클래스
data class TrafficResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<TrafficError>
)
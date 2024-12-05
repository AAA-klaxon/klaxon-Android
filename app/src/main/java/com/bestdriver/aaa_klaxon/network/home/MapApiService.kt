package com.bestdriver.aaa_klaxon.network.home

import retrofit2.http.GET
import retrofit2.http.Header

// Retrofit 인터페이스
interface MapApiService {
    @GET("errors/traffic")
    suspend fun getTrafficErrors(
    ): TrafficResponse
}
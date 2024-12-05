package com.bestdriver.aaa_klaxon.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.bestdriver.aaa_klaxon.network.auth.AuthApiService
import com.bestdriver.aaa_klaxon.network.community.CommunityApiService
import com.bestdriver.aaa_klaxon.network.home.MapApiService
import com.bestdriver.aaa_klaxon.network.mypage.MypageApiService
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL = "http://43.202.104.135:3000/"

    @Volatile
    private var retrofit: Retrofit? = null

    private fun getRetrofitInstance(context: Context): Retrofit {
        return retrofit ?: synchronized(this) {
            val tokenManager = TokenManager(context)

            val authApiService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(AuthApiService::class.java)

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor(tokenManager, authApiService))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            retrofit!!
        }
    }

    fun getAuthApiService(context: Context): AuthApiService {
        return getRetrofitInstance(context).create(AuthApiService::class.java)
    }

    fun getMypageApiService(context: Context): MypageApiService {
        return getRetrofitInstance(context).create(MypageApiService::class.java)
    }

    // 필요한 다른 API 서비스 인스턴스 제공
    fun getCommunityApiService(context: Context): CommunityApiService {
        return getRetrofitInstance(context).create(CommunityApiService::class.java)
    }

    // 필요한 다른 API 서비스 인스턴스 제공
    fun getMapApiService(context: Context): MapApiService {
        return getRetrofitInstance(context).create(MapApiService::class.java)
    }
}

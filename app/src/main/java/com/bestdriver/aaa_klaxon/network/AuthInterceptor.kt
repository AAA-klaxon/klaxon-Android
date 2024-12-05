package com.bestdriver.aaa_klaxon.network

import android.util.Log
import com.bestdriver.aaa_klaxon.network.auth.AuthApiService
import com.bestdriver.aaa_klaxon.network.auth.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val authApiService: AuthApiService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url

        // 로그인 및 회원가입 요청에는 토큰을 추가하지 않음
        if (url.encodedPath.contains("/login") || url.encodedPath.contains("/signup")) {
            return chain.proceed(request)
        }

        // 액세스 토큰 추가
        val accessToken = tokenManager.getAccessToken()
        val newRequest = request.newBuilder().apply {
            if (!accessToken.isNullOrEmpty()) {
                addHeader("Authorization", accessToken)
            }
        }.build()

        var response = chain.proceed(newRequest)

        // 액세스 토큰이 만료된 경우 (401 Unauthorized)
        if (response.code == 401) {
            val refreshToken = tokenManager.getRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {
                // 기존 응답을 닫음
                response.close()

                // 새로운 토큰 발급 요청
                val refreshResponse = runBlocking {
                    authApiService.refreshToken(RefreshTokenRequest(refreshToken))
                }

                if (refreshResponse.isSuccessful) {
                    val newAccessToken = refreshResponse.headers()["Authorization"]
                    val newRefreshToken = refreshResponse.body()?.result?.refreshToken

                    if (!newAccessToken.isNullOrEmpty() && !newRefreshToken.isNullOrEmpty()) {
                        // 새로운 액세스 토큰과 리프레시 토큰 저장
                        tokenManager.saveAccessToken(newAccessToken)
                        tokenManager.saveRefreshToken(newRefreshToken)

                        // 새로운 액세스 토큰으로 요청 재시도
                        val retryRequest = request.newBuilder()
                            .header("Authorization", newAccessToken)
                            .build()
                        response = chain.proceed(retryRequest)
                    } else {
                        // 토큰 갱신 실패 시 모든 토큰 초기화
                        tokenManager.clearTokens()
                    }
                } else {
                    // 토큰 갱신 실패 시 모든 토큰 초기화
                    tokenManager.clearTokens()
                }
            }
        }
        return response
    }
}


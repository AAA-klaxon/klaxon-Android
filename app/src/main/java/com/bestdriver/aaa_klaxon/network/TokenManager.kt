package com.bestdriver.aaa_klaxon.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getAccessToken(): String? {
        val token = sharedPreferences.getString("ACCESS_TOKEN", null)
        if (token.isNullOrEmpty()) {
            Log.w("TokenManager", "Access token is null or empty")
        }
        return token
    }

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("ACCESS_TOKEN", token).apply()
        Log.d("TokenManager", "Access token saved: $token")
        // 저장된 토큰 확인
        Log.d("TokenManager", "Retrieved access token: ${getAccessToken()}")
    }

    fun getRefreshToken(): String? {
        val token = sharedPreferences.getString("REFRESH_TOKEN", null)
        if (token.isNullOrEmpty()) {
            Log.w("TokenManager", "Refresh token is null or empty")
        }
        return token
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString("REFRESH_TOKEN", token).apply()
        Log.d("TokenManager", "Refresh token saved: $token")
        // 저장된 토큰 확인
        Log.d("TokenManager", "Retrieved refresh token: ${getRefreshToken()}")
    }

    fun clearTokens() {
        val success = sharedPreferences.edit()
            .remove("ACCESS_TOKEN")
            .remove("REFRESH_TOKEN")
            .commit()
        if (success) {
            Log.d("TokenManager", "Tokens cleared successfully")
        } else {
            Log.e("TokenManager", "Failed to clear tokens")
        }
    }
}




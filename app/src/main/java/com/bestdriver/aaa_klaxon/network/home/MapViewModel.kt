package com.bestdriver.aaa_klaxon.network.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestdriver.aaa_klaxon.network.RetrofitClient
import com.bestdriver.aaa_klaxon.network.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _trafficData = MutableStateFlow<List<TrafficError>>(emptyList())
    val trafficData: StateFlow<List<TrafficError>> get() = _trafficData

    private val apiService: MapApiService = RetrofitClient.getMapApiService(application)

    init {
        fetchTrafficErrors()
    }

    fun fetchTrafficErrors() {
        viewModelScope.launch {
            try {
                val response = apiService.getTrafficErrors()
                if (response.isSuccess) {
                    val trafficErrors = response.result ?: emptyList()
                    Log.d("MapViewModel", "Fetched traffic errors: $trafficErrors")
                    _trafficData.value = trafficErrors
                } else {
                    Log.e("MapViewModel", "Traffic errors fetching failed: ${response.code} - ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Exception fetching traffic errors: ${e.localizedMessage}")
            }
        }
    }
}


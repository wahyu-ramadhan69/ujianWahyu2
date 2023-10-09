package com.sepuh.ujian2.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {
    private const val BASE_URL = "https://vrtqq7sn-8080.asse.devtunnels.ms/" // Ganti dengan URL API Anda

    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

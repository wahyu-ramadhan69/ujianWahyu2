package com.sepuh.ujian2.services

import com.sepuh.ujian2.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("ujian/")
    fun getUsers(): Call<List<User>>

    @POST("ujian/")
    fun addUser(@Body user: User): Call<User>

    @PUT("ujian/{id}")
    fun editUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("ujian/{id}")
    fun hapusUser(@Path("id") id: Int): Call<Void>

    @GET("ujian/cari")
    fun searchUsers(@Query("nama") query: String): Call<List<User>>
}

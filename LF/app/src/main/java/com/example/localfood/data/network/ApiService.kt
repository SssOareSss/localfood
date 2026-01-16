package com.example.localfood.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import kotlin.jvm.JvmSuppressWildcards

interface ApiService {
    @POST("register.php")
    suspend fun register(@Body user: Map<String, String>): Map<String, Any>

    @POST("login.php")
    suspend fun login(@Body user: Map<String, String>): Map<String, Any>

    @GET("vendedores.php")
    suspend fun getVendedores(): List<Map<String, Any>>
}


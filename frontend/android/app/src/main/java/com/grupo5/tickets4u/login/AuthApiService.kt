package com.grupo5.tickets4u.login

import com.grupo5.tickets4u.login.RegisterRequest
import com.grupo5.tickets4u.login.RegisterResponse
import com.grupo5.tickets4u.login.LoginRequest
import com.grupo5.tickets4u.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}

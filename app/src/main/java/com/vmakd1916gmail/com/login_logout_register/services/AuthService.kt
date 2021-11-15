package com.vmakd1916gmail.com.login_logout_register.services

import com.vmakd1916gmail.com.login_logout_register.models.network.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/users/")
    suspend fun registerUser(@Body user: UserResponse?): Response<UserResponse>

    @POST("auth/jwt/create/")
    suspend fun authUser(@Body user: UserResponse): Response<TokenResponse>

}
package com.vmakd1916gmail.com.mysocialnetwork.services

import com.vmakd1916gmail.com.mysocialnetwork.models.network.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/users/")
    suspend fun registerUser(@Body user: UserResponse?): Response<UserResponse>

    @POST("auth/jwt/create/")
    suspend fun authUser(@Body user: UserResponse): Response<TokenResponse>

    @POST("auth/jwt/refresh/")
    suspend fun refreshToken(@Body token: RefreshTokenResponse): Response<AccessTokenResponse>

}
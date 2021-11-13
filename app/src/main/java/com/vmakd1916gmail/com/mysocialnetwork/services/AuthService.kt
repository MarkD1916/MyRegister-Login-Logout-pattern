package com.vmakd1916gmail.com.mysocialnetwork.services

import com.vmakd1916gmail.com.mysocialnetwork.models.network.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/users/")
    fun registerUser(@Body user: UserResponse?): Call<UserResponse?>?

    @POST("auth/jwt/create/")
    fun authUser(@Body user: UserResponse?): Call<TokenResponse>

    @POST("auth/jwt/refresh/")
    fun refreshToken(@Body token: RefreshTokenResponse): Call<AccessTokenResponse?>?

}
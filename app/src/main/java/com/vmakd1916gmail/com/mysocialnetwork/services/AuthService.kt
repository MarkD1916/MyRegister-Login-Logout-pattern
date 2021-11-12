package com.vmakd1916gmail.com.mysocialnetwork.services

import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @POST("auth/users/")
    fun registerUser(@Body user: UserResponse?): Call<UserResponse?>?

    @POST("auth/jwt/create/")
    fun authUser(@Body user: UserResponse?): Call<TokenResponse>

    @POST("auth/jwt/refresh/")
    fun refreshToken(@Body refreshToken:String): Call<String?>?


    @POST("auth/jwt/verify/")
    fun verifyToken(@Body token:AccessTokenResponse): Call<AccessTokenResponse?>?
}
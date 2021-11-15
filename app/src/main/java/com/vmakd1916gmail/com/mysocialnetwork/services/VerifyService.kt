package com.vmakd1916gmail.com.mysocialnetwork.services

import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VerifyService {
    @POST("auth/jwt/verify/")
    suspend fun verifyToken(@Body token: VerifyTokenResponse?): Response<VerifyTokenResponse>
}
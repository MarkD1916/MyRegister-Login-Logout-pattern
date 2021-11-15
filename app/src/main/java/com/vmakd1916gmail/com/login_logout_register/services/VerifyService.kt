package com.vmakd1916gmail.com.login_logout_register.services

import com.vmakd1916gmail.com.login_logout_register.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.login_logout_register.models.network.RefreshTokenResponse
import com.vmakd1916gmail.com.login_logout_register.models.network.VerifyTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VerifyService {
    @POST("auth/jwt/verify/")
    suspend fun verifyToken(@Body token: VerifyTokenResponse?): Response<VerifyTokenResponse>

    @POST("auth/jwt/refresh/")
    suspend fun refreshToken(@Body token: RefreshTokenResponse): Response<AccessTokenResponse>
}
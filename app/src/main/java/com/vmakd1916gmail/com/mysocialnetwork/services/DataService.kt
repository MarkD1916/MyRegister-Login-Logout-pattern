package com.vmakd1916gmail.com.mysocialnetwork.services

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers


interface DataService {

    @GET("restricted/")
    suspend fun getDataLoginUser(@Header("Authorization") accessToken:String): Response<String>

    @GET("checkserver/")
    fun getDataAllUser(): Call<String?>?
}
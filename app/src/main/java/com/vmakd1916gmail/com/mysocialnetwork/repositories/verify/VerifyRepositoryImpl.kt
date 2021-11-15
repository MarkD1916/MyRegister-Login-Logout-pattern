package com.vmakd1916gmail.com.mysocialnetwork.repositories.verify

import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.RefreshTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.Resource
import com.vmakd1916gmail.com.mysocialnetwork.other.getAuthDataFromServer
import com.vmakd1916gmail.com.mysocialnetwork.other.safeCall
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.Variables
import com.vmakd1916gmail.com.mysocialnetwork.services.VerifyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class VerifyRepositoryImpl @Inject constructor(
    private val verifyService: VerifyService) {

    suspend fun verifyToken(token: VerifyTokenResponse?): Resource<Response<VerifyTokenResponse>> {


        return withContext(Dispatchers.IO) {
            safeCall {
                if (!Variables.isNetworkConnected){
                    throw Exception("No Internet connection")
                }
                val call = verifyService.verifyToken(token)
                val result = getAuthDataFromServer(call)
                Resource.Success(result)
            }
        }
    }

    suspend fun refreshToken(refreshTokenResponse: RefreshTokenResponse): Resource<Response<AccessTokenResponse>> {


        return withContext(Dispatchers.IO) {
            safeCall {
                if (!Variables.isNetworkConnected){
                    throw Exception("No Internet connection")
                }
                val call = verifyService.refreshToken(refreshTokenResponse)
                val result = getAuthDataFromServer(call)
                Resource.Success(result)
            }
        }
    }

}
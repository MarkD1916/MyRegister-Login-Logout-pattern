package com.vmakd1916gmail.com.mysocialnetwork.repositories.verify

import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.Resource
import com.vmakd1916gmail.com.mysocialnetwork.other.getAuthDataFromServer
import com.vmakd1916gmail.com.mysocialnetwork.other.safeCall
import com.vmakd1916gmail.com.mysocialnetwork.services.VerifyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class VerifyRepositoryImpl @Inject constructor(
    private val verifyService: VerifyService) {

    suspend fun verifyToken(token: VerifyTokenResponse?): Resource<Response<VerifyTokenResponse>> {
        val call = verifyService.verifyToken(token)
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = getAuthDataFromServer(call)
                Resource.Success(result)
            }
        }
    }

}
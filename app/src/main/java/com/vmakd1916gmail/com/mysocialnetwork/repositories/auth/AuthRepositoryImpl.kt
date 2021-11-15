package com.vmakd1916gmail.com.mysocialnetwork.repositories.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.*
import com.vmakd1916gmail.com.mysocialnetwork.other.Resource
import com.vmakd1916gmail.com.mysocialnetwork.other.getAuthDataFromServer
import com.vmakd1916gmail.com.mysocialnetwork.other.safeCall
import com.vmakd1916gmail.com.mysocialnetwork.services.AuthService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val mySocialNetworkDAO: MySocialNetworkDAO
) {



    suspend fun registerUser(userResponse: UserResponse):Resource<Response<UserResponse>> {

        return withContext(Dispatchers.IO) {
            safeCall {
                if (!Variables.isNetworkConnected){
                    throw Exception("No Internet connection")
                }
                val call = authService.registerUser(userResponse)
                val result = getAuthDataFromServer(call)
                Resource.Success(result)
            }
        }
    }


    suspend fun authUser(user: UserResponse): Resource<Response<TokenResponse>> {

        return withContext(Dispatchers.IO) {
            safeCall {
                if (!Variables.isNetworkConnected){
                    throw Exception("No Internet connection")
                }
                val call = authService.authUser(user)
                val result = getAuthDataFromServer(call)
                Resource.Success(result)
            }
        }
    }




    fun getToke(): LiveData<Token> {
        return mySocialNetworkDAO.getToken()
    }

    suspend fun insertToken(token: Token) {
        mySocialNetworkDAO.insertToken(token)
    }


}
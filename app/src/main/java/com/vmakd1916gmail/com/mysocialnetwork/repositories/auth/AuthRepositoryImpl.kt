package com.vmakd1916gmail.com.mysocialnetwork.repositories.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.*
import com.vmakd1916gmail.com.mysocialnetwork.services.AuthService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val mySocialNetworkDAO: MySocialNetworkDAO
) {


    fun registerUser(user: UserResponse): LiveData<RegisterStatus> {
        val call = authService.registerUser(user)
        val registerStatus = MutableLiveData<RegisterStatus>()
        call?.enqueue(object : Callback<UserResponse?> {
            override fun onFailure(call: Call<UserResponse?>, t: Throwable?) {
                registerStatus.value = RegisterStatus.FAIL
            }

            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {

                if (response.code() == 400) {
                    registerStatus.value = RegisterStatus.FAIL
                }
                if (response.code() == 201) {
                    registerStatus.value = RegisterStatus.SUCCESS
                }
            }
        })
        return registerStatus
    }


    fun authUser(user: UserResponse): LiveData<AuthStatus> {
        val call = authService.authUser(user)
        var authStatus = MutableLiveData<AuthStatus>()
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.code() == 200) {

                    val token = response.body()?.let { createToken(it) }
                    GlobalScope.launch(Dispatchers.IO) {
                        if (token != null) {
                            insertToken(token){
                                authStatus.postValue(AuthStatus.SUCCESS)
                            }
                        }
                    }
                } else {
                    authStatus.postValue(AuthStatus.FAIL)
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                authStatus.postValue(AuthStatus.FAIL)
            }
        }
        )
        return authStatus
    }


    fun refreshToken(refreshTokenResponse: RefreshTokenResponse):LiveData<RefreshStatus> {
        val call = authService.refreshToken(refreshTokenResponse)
        val refreshStatus = MutableLiveData<RefreshStatus>()

        call?.enqueue(object : Callback<AccessTokenResponse?> {
            override fun onResponse(call: Call<AccessTokenResponse?>, response: Response<AccessTokenResponse?>) {
                val accessToken = response.body()?.access_token
                GlobalScope.launch(Dispatchers.IO) {
                if (accessToken!=null) {
                    mySocialNetworkDAO.updateAccessToken(accessToken)
                    refreshStatus.postValue(RefreshStatus.SUCCESS)
                }
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse?>, t: Throwable) {
            }
        }
        )
        return refreshStatus
    }

    fun getToke(): LiveData<Token> {
        return mySocialNetworkDAO.getToken()
    }

    suspend fun insertToken(token: Token, onSuccess:()->Unit) {
        mySocialNetworkDAO.insertToken(token)
        onSuccess()
    }

    private fun createToken(tokenResponse: TokenResponse): Token {
        return Token(
            UUID.randomUUID(),
            tokenResponse.refresh_token,
            tokenResponse.access_token
        )
    }
}
package com.vmakd1916gmail.com.mysocialnetwork.repositories.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.services.AuthService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        val registerLiveData = MutableLiveData<RegisterStatus>()
        call?.enqueue(object : Callback<UserResponse?> {
            override fun onFailure(call: Call<UserResponse?>, t: Throwable?) {
                registerLiveData.value = RegisterStatus.FAIL
            }

            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {

                if (response.code() == 400) {
                    registerLiveData.value = RegisterStatus.FAIL
                }
                if (response.code() == 201) {
                    registerLiveData.value = RegisterStatus.SUCCESS
                }
            }
        })
        return registerLiveData
    }


    fun authUser(user: UserResponse): LiveData<AuthStatus> {
        val call = authService.authUser(user)
        var tokenResponse = MutableLiveData<AuthStatus>()
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.code() == 200) {
                    tokenResponse.postValue(AuthStatus.SUCCESS)
                    val token = response.body()?.let { createToken(it) }
                    GlobalScope.launch(Dispatchers.IO) {
                        if (token != null) {
                            insertToken(token)
                        }
                        Log.d(TAG, "onResponse: ping")
                    }


                } else {
                    tokenResponse.postValue(AuthStatus.FAIL)
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                tokenResponse.postValue(AuthStatus.FAIL)
            }
        }
        )
        return tokenResponse
    }


    fun refreshToken(tokenId: UUID, refresh_token: String) {
        val call = authService.refreshToken(refresh_token)


        call?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        }
        )
    }


    fun verifyToken(token: AccessTokenResponse): LiveData<TokenVerifyStatus> {
        val call = authService.verifyToken(token)
        val registerLiveData = MutableLiveData<TokenVerifyStatus>()
        call?.enqueue(object : Callback<AccessTokenResponse?> {
            override fun onFailure(call: Call<AccessTokenResponse?>, t: Throwable?) {
                registerLiveData.value = TokenVerifyStatus.FAIL
            }

            override fun onResponse(
                call: Call<AccessTokenResponse?>,
                response: Response<AccessTokenResponse?>
            ) {
                if (response.code() == 400) {
                    registerLiveData.value = TokenVerifyStatus.FAIL
                }
                if (response.code() == 200) {
                    registerLiveData.value = TokenVerifyStatus.SUCCESS
                }
            }
        })
        return registerLiveData
    }

    fun getToke(): LiveData<Token> {
        return mySocialNetworkDAO.getToken()
    }

    suspend fun insertToken(token: Token) {
        mySocialNetworkDAO.insertToken(token)
    }

    private fun createToken(tokenResponse: TokenResponse): Token {
        return Token(
            UUID.randomUUID(),
            tokenResponse.refresh_token,
            tokenResponse.access_token
        )
    }

}
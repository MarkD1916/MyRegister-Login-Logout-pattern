package com.vmakd1916gmail.com.mysocialnetwork.repositories.auth

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.UserAndToken
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
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

    //ToDo Нужно добавить конкретные сообщения от чего ебается: уже зареган, пароль херовый, время ответа сервера слишком долгое в общем по документации посмотреть
    fun registerUser(user: UserResponse):LiveData<RegisterStatus> {
        val call = authService.registerUser(user)
        val registerLiveData = MutableLiveData<RegisterStatus>()
        call?.enqueue(object : Callback<UserResponse?> {
            override fun onFailure(call: Call<UserResponse?>, t: Throwable?) {
                registerLiveData.value = RegisterStatus.FAIL

            }
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {

                if (response.code()==400){
                    registerLiveData.value = RegisterStatus.FAIL
                }
                if (response.code()==201) {
                    registerLiveData.value = RegisterStatus.SUCCESS
                }


            }
        })
        return registerLiveData
    }

    //ToDo Та же история, успешную авторизацию нужно отслеживать, неуспешную тоже
    fun authUser(user: UserResponse): MutableLiveData<TokenResponse> {
        val call = authService.authUser(user)
        val tokenResponse = MutableLiveData<TokenResponse>()
        call?.enqueue(object : Callback<TokenResponse?> {
            override fun onResponse(
                call: Call<TokenResponse?>,
                response: Response<TokenResponse?>
            ) {
                tokenResponse.value = response.body()
            }

            override fun onFailure(call: Call<TokenResponse?>, t: Throwable) {
            }
        }
        )
        return tokenResponse
    }

    //ToDo вот эта хрень для того чтобы у нас не наебывался вход в приложение - когда токен не валиден его нужно рефрешить
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

    //ToDo то же самое причины наебывания
    fun verifyToken(token:AccessTokenResponse):LiveData<RegisterStatus>{
        val call = authService.verifyToken(token)
        val registerLiveData = MutableLiveData<RegisterStatus>()
        call?.enqueue(object : Callback<AccessTokenResponse?> {
            override fun onFailure(call: Call<AccessTokenResponse?>, t: Throwable?) {
                registerLiveData.value = RegisterStatus.FAIL
                Log.d(TAG, "verifyToken onFailure: $t")

            }

            override fun onResponse(call: Call<AccessTokenResponse?>, response: Response<AccessTokenResponse?>) {
                Log.d(TAG, "verifyToken onFailure: $response")
                if (response.code()==400){
                    registerLiveData.value = RegisterStatus.FAIL

                }
                if (response.code()==200) {
                    registerLiveData.value = RegisterStatus.SUCCESS
                }


            }
        })
        return registerLiveData
    }

    fun getCurrentActiveUser(loginStatus: LoginUserStatus): LiveData<User> {
        return mySocialNetworkDAO.getCurrentAuthUser(loginStatus)
    }

    fun getTokenByUserId(user_id: UUID): LiveData<List<UserAndToken>> {
        return mySocialNetworkDAO.getTokenByUserId(user_id)
    }

    fun getUser():LiveData<List<User>>{
       return mySocialNetworkDAO.getUserFromDB()
    }

    fun getUserAndToken():LiveData<List<User>>{
        return mySocialNetworkDAO.getUserFromDB()
    }

    suspend fun insertToken(token: Token) {
        mySocialNetworkDAO.insertToken(token)
    }

    suspend fun insertUser(user: User) {
        mySocialNetworkDAO.insertUser(user)
    }


    suspend fun updateUserStatus(loginStatus: LoginUserStatus,user_id: UUID){
        mySocialNetworkDAO.updateLoginStatus(loginStatus,user_id)

    }


}
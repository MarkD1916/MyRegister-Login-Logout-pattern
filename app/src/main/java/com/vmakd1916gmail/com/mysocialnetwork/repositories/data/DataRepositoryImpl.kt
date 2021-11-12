package com.vmakd1916gmail.com.mysocialnetwork.repositories.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.TokenVerifyStatus
import com.vmakd1916gmail.com.mysocialnetwork.services.AuthService
import com.vmakd1916gmail.com.mysocialnetwork.services.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val TAG = "DataRepositoryImpl"

class DataRepositoryImpl @Inject constructor(
    private val dataService: DataService,
    private val authService: AuthService,
    private val mySocialNetworkDAO: MySocialNetworkDAO
) {

    fun getDataForLoginUser(accessToken: String): LiveData<String> {

        val call = dataService.getDataLoginUser("Bearer $accessToken")
        val liveData = MutableLiveData<String>()
        call?.enqueue(object : Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable?) {


            }

            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                liveData.value = response.body()
            }
        })
        return liveData
    }



    fun getDataAllUser(): LiveData<String> {
        val call = dataService.getDataAllUser()
        val liveData = MutableLiveData<String>()
        call?.enqueue(object : Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable?) {


            }

            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                liveData.value = response.body()
            }
        })
        return liveData
    }


    fun verifyToken(token:AccessTokenResponse):LiveData<TokenVerifyStatus>{
        val call = authService.verifyToken(token)
        val registerLiveData = MutableLiveData<TokenVerifyStatus>()
        call?.enqueue(object : Callback<AccessTokenResponse?> {
            override fun onFailure(call: Call<AccessTokenResponse?>, t: Throwable?) {
                registerLiveData.value = TokenVerifyStatus.FAIL
            }
            override fun onResponse(call: Call<AccessTokenResponse?>, response: Response<AccessTokenResponse?>) {
                if (response.code()==400){
                    registerLiveData.value = TokenVerifyStatus.FAIL
                }
                if (response.code()==200) {
                    registerLiveData.value = TokenVerifyStatus.SUCCESS
                }
            }
        })
        return registerLiveData
    }

    fun getToke(): LiveData<Token> {
        return mySocialNetworkDAO.getToken()
    }

    suspend fun deleteToken(token:Token){
        mySocialNetworkDAO.deleteToken(token)
    }
}
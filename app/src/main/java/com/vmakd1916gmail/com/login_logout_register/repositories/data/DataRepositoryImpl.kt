package com.vmakd1916gmail.com.login_logout_register.repositories.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.login_logout_register.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.login_logout_register.models.Token
import com.vmakd1916gmail.com.login_logout_register.other.Resource
import com.vmakd1916gmail.com.login_logout_register.other.getAuthDataFromServer
import com.vmakd1916gmail.com.login_logout_register.other.safeCall
import com.vmakd1916gmail.com.login_logout_register.repositories.auth.Variables
import com.vmakd1916gmail.com.login_logout_register.services.DataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val TAG = "DataRepositoryImpl"

class DataRepositoryImpl @Inject constructor(
    private val dataService: DataService,
    private val mySocialNetworkDAO: MySocialNetworkDAO
) {

    suspend fun getDataForLoginUser(accessToken: String?): Resource<Response<String>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                if (!Variables.isNetworkConnected) {
                    throw Exception("No Internet connection")
                }
                val call = dataService.getDataLoginUser()
                val result = getAuthDataFromServer(call)
                Resource.Success(result)
            }
        }
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


    fun getToke(): LiveData<Token> {
        return mySocialNetworkDAO.getToken()
    }

    fun deleteToken(token: Token) {
        mySocialNetworkDAO.deleteToken(token)
    }
}
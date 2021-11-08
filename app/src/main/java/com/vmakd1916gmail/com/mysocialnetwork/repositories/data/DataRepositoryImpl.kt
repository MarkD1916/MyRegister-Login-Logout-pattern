package com.vmakd1916gmail.com.mysocialnetwork.repositories.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.UserAndToken
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.services.AuthService
import com.vmakd1916gmail.com.mysocialnetwork.services.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
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

    fun getCurrentActiveUser(loginStatus: LoginUserStatus): LiveData<User> {
        return mySocialNetworkDAO.getCurrentAuthUser(loginStatus)
    }

    fun getTokenByUserId(user_id: UUID): LiveData<List<UserAndToken>> {
        return mySocialNetworkDAO.getTokenByUserId(user_id)
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

    suspend fun logoutUser(token: Token){
        mySocialNetworkDAO.deleteToken(token)
    }

    suspend fun updateUserStatus(loginStatus: LoginUserStatus,user_id: UUID){
        mySocialNetworkDAO.updateLoginStatus(loginStatus,user_id)
    }
}
package com.vmakd1916gmail.com.mysocialnetwork.repositories.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.Resource
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.TokenVerifyStatus
import com.vmakd1916gmail.com.mysocialnetwork.services.VerifyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class VerifyRepositoryImpl @Inject constructor(
    private val verifyService: VerifyService) {

    fun verifyToken(token: VerifyTokenResponse): LiveData<TokenVerifyStatus> {
        val call = verifyService.verifyToken(token)
        val verifyStatus = MutableLiveData<TokenVerifyStatus>()
        call?.enqueue(object : Callback<VerifyTokenResponse?> {
            override fun onFailure(call: Call<VerifyTokenResponse?>, t: Throwable?) {
                verifyStatus.value = TokenVerifyStatus.FAIL
            }
            override fun onResponse(call: Call<VerifyTokenResponse?>, response: Response<VerifyTokenResponse?>) {
                if (response.code()==401){
                    verifyStatus.value = TokenVerifyStatus.FAIL
                }
                if (response.code()==200) {
                    verifyStatus.value = TokenVerifyStatus.SUCCESS
                }
            }
        })
        return verifyStatus
    }
}
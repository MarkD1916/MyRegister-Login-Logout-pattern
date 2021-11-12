package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.TokenVerifyStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.data.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "DataViewModel"


@HiltViewModel
class DataViewModel @Inject constructor(private val repository: DataRepositoryImpl) : ViewModel() {

    fun getDataForLoginUser(accessToken: String): LiveData<String> {
        return repository.getDataForLoginUser(accessToken)
    }

    fun verifyToken(token: AccessTokenResponse): LiveData<TokenVerifyStatus> {
        return repository.verifyToken(token)
    }

    fun getToken(): LiveData<Token> {
        return repository.getToke()
    }

    fun getDataForAllUser(): LiveData<String> {
        return repository.getDataAllUser()
    }

    fun deleteToken(token: Token) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteToken(token)
            }
        }
    }
}
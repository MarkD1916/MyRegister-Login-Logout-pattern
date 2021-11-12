package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.AuthRepositoryImpl
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.AuthStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.RegisterStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.TokenVerifyStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepositoryImpl) : ViewModel() {

    fun registerUser(user: UserResponse): LiveData<RegisterStatus> {
        return repository.registerUser(user)
    }



    fun authUser(user: UserResponse): LiveData<AuthStatus> {
        return repository.authUser(user)

    }

    fun getToken():LiveData<Token>{
        return repository.getToke()
    }


    fun insertToken(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertToken(token)
        }
    }


    fun verifyToken(token: AccessTokenResponse): LiveData<TokenVerifyStatus> {
        return repository.verifyToken(token)
    }


    fun createUserResponse(userName: String, userPassword: String): UserResponse {
        return UserResponse(userName, userPassword)
    }

}
package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.RefreshTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.*
import com.vmakd1916gmail.com.mysocialnetwork.repositories.verify.VerifyRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val verifyRepository: VerifyRepositoryImpl
) : ViewModel() {

    fun registerUser(user: UserResponse): LiveData<RegisterStatus> {
        return repository.registerUser(user)
    }


    fun authUser(user: UserResponse): LiveData<AuthStatus> {
        return repository.authUser(user)

    }

    fun getToken(): LiveData<Token> {
        return repository.getToke()
    }


    fun insertToken(token: Token, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertToken(token, onSuccess)
            onSuccess()
        }
    }

    fun refreshToken(refreshTokenResponse: RefreshTokenResponse): LiveData<RefreshStatus> {
        return repository.refreshToken(refreshTokenResponse)
    }

    fun verifyToken(token: VerifyTokenResponse): LiveData<TokenVerifyStatus> {
        return verifyRepository.verifyToken(token)
    }


    fun createUserResponse(userName: String, userPassword: String): UserResponse {
        return UserResponse(userName, userPassword)
    }

}
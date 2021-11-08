package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM

import androidx.lifecycle.*
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.UserAndToken
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepositoryImpl) : ViewModel() {

    fun registerUser(user: UserResponse):LiveData<RegisterStatus> {
        return repository.registerUser(user)
    }


    fun authUser(user: UserResponse): LiveData<AuthStatus> {
        return repository.authUser(user)
    }


    fun insertToken(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertToken(token)
        }
    }

    fun getUserFromDB(): LiveData<List<User>> {
        return repository.getUser()
    }

    fun getCurrentActiveUser(loginStatus: LoginUserStatus): LiveData<User>{
        return repository.getCurrentActiveUser(loginStatus)
    }

    fun getTokenByUserId(user_id: UUID): LiveData<List<UserAndToken>>{
        return repository.getTokenByUserId(user_id)
    }

    fun verifyToken(token:AccessTokenResponse):LiveData<TokenVerifyStatus>{
        return repository.verifyToken(token)
    }


    fun createUserResponse(userName: String, userPassword: String): UserResponse {
        return UserResponse(userName, userPassword)
    }

}
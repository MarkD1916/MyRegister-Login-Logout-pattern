package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM

import androidx.lifecycle.*
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.UserAndToken
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.AuthRepositoryImpl
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.RegisterStatus
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


    fun authUser(user: UserResponse): LiveData<TokenResponse> {
        return repository.authUser(user)
    }



//    fun getAccessToken(): String {
//        //нужно достать токен из БД и дальше его радостно юзать
//        return ""
//    }
//
//    fun getRefreshToken(): String {
//        //нужно достать токен из БД и дальше его радостно юзать
//        return ""
//    }
//
//    fun getTokenResponse(): TokenResponse{
//
//    }

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
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

    fun verifyToken(token:AccessTokenResponse):LiveData<RegisterStatus>{
        return repository.verifyToken(token)
    }

    fun updateUserStatus(loginStatus: LoginUserStatus, user_id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserStatus(loginStatus, user_id)
        }

    }
}
package com.vmakd1916gmail.com.login_logout_register.ui.auth.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmakd1916gmail.com.login_logout_register.models.Token
import com.vmakd1916gmail.com.login_logout_register.models.local.User
import com.vmakd1916gmail.com.login_logout_register.models.network.*
import com.vmakd1916gmail.com.login_logout_register.other.Event
import com.vmakd1916gmail.com.login_logout_register.other.Resource
import com.vmakd1916gmail.com.login_logout_register.repositories.auth.*
import com.vmakd1916gmail.com.login_logout_register.repositories.verify.VerifyRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val verifyRepository: VerifyRepositoryImpl
) : ViewModel() {

    private val _registerStatus = MutableLiveData<Event<Resource<User>>>()
    val registerStatus:LiveData<Event<Resource<User>>> = _registerStatus

    fun registerUser(user: UserResponse) {
        _registerStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.registerUser(user)
            if (response.data!=null) {
                val user = createUser(response.data.body())
                // ToDo insert user хотя в рамках проекта нафиг не надо
                _registerStatus.postValue(Event(Resource.Success(user)))
            }
            else {
                _registerStatus.postValue(Event(Resource.Error(response.message!!)))
            }

        }
    }

    private val _authStatus = MutableLiveData<Event<Resource<Token>>>()
    val authStatus:LiveData<Event<Resource<Token>>> = _authStatus

    private val _accessTokenResponse = MutableLiveData<Event<Resource<AccessTokenResponse>>>()
    val accessTokenResponse:LiveData<Event<Resource<AccessTokenResponse>>> = _accessTokenResponse

    private val _verifyTokenResponse = MutableLiveData<Event<Resource<TokenVerifyStatus>>>()
    val verifyTokenResponse:LiveData<Event<Resource<TokenVerifyStatus>>> = _verifyTokenResponse

    fun authUser(user: UserResponse) {
        _authStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.authUser(user)
            if (response.data!=null) {
                val token = createToken(response.data.body())
                insertToken(token)
                _authStatus.postValue(Event(Resource.Success(token)))
            }
            else {
                _authStatus.postValue(Event(Resource.Error(response.message!!)))
            }

        }

    }

    fun verifyToken(token: VerifyTokenResponse) {
        _verifyTokenResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val response = verifyRepository.verifyToken(token)
            if (response.data!=null) {
                _verifyTokenResponse.postValue(Event(Resource.Success(TokenVerifyStatus.SUCCESS)))
            }
            else {
                _verifyTokenResponse.postValue(Event(Resource.Error(response.message!!)))
            }

        }
    }

    fun refreshToken(refreshTokenResponse: RefreshTokenResponse) {
        _accessTokenResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val response = verifyRepository.refreshToken(refreshTokenResponse)
            if (response.data!=null) {
                _accessTokenResponse.postValue(Event(Resource.Success(response.data.body()!!)))
            }
            else {
                _accessTokenResponse.postValue(Event(Resource.Error(response.message!!)))
            }
        }
    }

    private fun createToken(tokenResponse: TokenResponse?): Token {
        return Token(
            UUID.randomUUID(),
            tokenResponse?.refresh_token,
            tokenResponse?.access_token
        )
    }

    private fun createUser(user: UserResponse?): User {
        return User(
            UUID.randomUUID(),
            user?.name
        )
    }

    fun getToken(): LiveData<Token> {
        return repository.getToke()
    }


    fun insertToken(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertToken(token)
        }
    }


    fun createUserResponse(userName: String, userPassword: String): UserResponse {
        return UserResponse(userName, userPassword)
    }

}
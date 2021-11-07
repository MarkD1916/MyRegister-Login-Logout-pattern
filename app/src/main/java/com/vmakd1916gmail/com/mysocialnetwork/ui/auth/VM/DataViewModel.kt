package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.UserAndToken
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.data.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val TAG = "DataViewModel"


@HiltViewModel
class DataViewModel @Inject constructor(private val repository: DataRepositoryImpl) : ViewModel() {

    fun getDataForLoginUser(accessToken: String): LiveData<String> {
        return repository.getDataForLoginUser(accessToken)
    }

    fun getCurrentActiveUser(loginStatus: LoginUserStatus): LiveData<User> {
        return repository.getCurrentActiveUser(loginStatus)
    }

    fun getTokenByUserId(user_id: UUID): LiveData<List<UserAndToken>> {
        return repository.getTokenByUserId(user_id)
    }

    fun getDataForAllUser(): LiveData<String> {
        return repository.getDataAllUser()
    }

    fun logoutUser(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logoutUser(token)
        }
    }

    fun updateUserStatus(loginStatus: LoginUserStatus, user_id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserStatus(loginStatus, user_id)
        }
    }
}
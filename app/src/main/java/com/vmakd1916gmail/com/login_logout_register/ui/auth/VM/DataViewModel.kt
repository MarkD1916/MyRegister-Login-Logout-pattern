package com.vmakd1916gmail.com.login_logout_register.ui.auth.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmakd1916gmail.com.login_logout_register.models.Token
import com.vmakd1916gmail.com.login_logout_register.other.Event
import com.vmakd1916gmail.com.login_logout_register.other.Resource
import com.vmakd1916gmail.com.login_logout_register.repositories.data.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "DataViewModel"


@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: DataRepositoryImpl
) : ViewModel() {

    private val _dataForLogginUserResponse = MutableLiveData<Event<Resource<String>>>()
    val dataForLogginUserResponse:LiveData<Event<Resource<String>>> = _dataForLogginUserResponse

    fun getDataForLoginUser(accessToken: String?) {
        _dataForLogginUserResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getDataForLoginUser(accessToken)
            if (response.data!=null) {
                _dataForLogginUserResponse.postValue(Event(Resource.Success(response.data.body()!!)))
            }
            else {
                _dataForLogginUserResponse.postValue(Event(Resource.Error(response.message!!)))
            }
        }
    }


    fun getToken(): LiveData<Token> {
        return repository.getToke()
    }

    fun getDataForAllUser(): LiveData<String> {
        return repository.getDataAllUser()
    }

    fun deleteToken(token: Token, onSuccess: () -> Unit) {
        onSuccess()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteToken(token)
            }
        }
    }
}
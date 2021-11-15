package com.vmakd1916gmail.com.login_logout_register.other

import com.vmakd1916gmail.com.login_logout_register.repositories.auth.Variables
import retrofit2.Response
import java.lang.Exception

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown error occurred")
    }
}

fun <T> getAuthDataFromServer(response: Response<T>): Response<T> {
    if (!Variables.isNetworkConnected){
        throw Exception("No Internet connection")
    }

    if (response.isSuccessful) {
        return response
    } else {
        throw Exception(response.message())
    }

}

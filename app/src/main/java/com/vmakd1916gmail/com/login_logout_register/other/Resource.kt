package com.vmakd1916gmail.com.login_logout_register.other

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T): Resource<T>(data)

    class Error<T>(message: String, data: T?=null): Resource<T>(data,message)

    class Loading<T>(data: T?=null): Resource<T>(data)
}
package com.vmakd1916gmail.com.login_logout_register.repositories.auth

import android.util.Log
import com.vmakd1916gmail.com.login_logout_register.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.login_logout_register.other.TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

private const val TAG = "RequestTokenInterceptor"

class RequestTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        Log.d(TAG, "intercept: ${request.method()}")

        when (request.method()) {
            "GET" -> {
                if (TOKEN!=null) {
                    val newRequest =
                        request.newBuilder().addHeader("Authorization", "Bearer $TOKEN")
                            .method(request.method(), request.body())
                            .build()
                    return chain.proceed(newRequest)
                }
                else{
                    val newRequest =
                        request.newBuilder()
                            .method(request.method(), request.body())
                            .build()
                    return chain.proceed(newRequest)
                }



            }
            else -> {
                val newRequest = request.newBuilder()
                    .build()
                return chain.proceed(newRequest)
            }
        }
    }

}
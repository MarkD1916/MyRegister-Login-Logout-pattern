package com.vmakd1916gmail.com.login_logout_register.repositories.auth

import android.util.Log
import kotlin.properties.Delegates


enum class TokenVerifyStatus {
    SUCCESS
}
object Variables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.d("Network connectivity", "$newValue")
    }
}
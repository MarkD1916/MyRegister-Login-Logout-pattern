package com.vmakd1916gmail.com.login_logout_register.models.network

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("refresh") val refresh_token: String,
    @SerializedName("access") val access_token: String
)
package com.vmakd1916gmail.com.mysocialnetwork.models.network

import com.google.gson.annotations.SerializedName

data class VerifyTokenResponse(
    @SerializedName("token") val access_token: String
)
package com.vmakd1916gmail.com.mysocialnetwork.models.network

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse (
    @SerializedName("refresh") val refresh_token: String?
)
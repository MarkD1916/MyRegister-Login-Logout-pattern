package com.vmakd1916gmail.com.mysocialnetwork.models.network

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

data class TokenResponse(
    @SerializedName("refresh") val refresh_token: String,
    @SerializedName("access") val access_token: String
)
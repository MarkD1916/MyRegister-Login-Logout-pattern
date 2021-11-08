package com.vmakd1916gmail.com.mysocialnetwork.models.network

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("username") val name:String,
    @SerializedName("password") val password:String,
)
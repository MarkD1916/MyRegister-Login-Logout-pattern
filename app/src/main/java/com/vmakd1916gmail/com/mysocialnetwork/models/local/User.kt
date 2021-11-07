package com.vmakd1916gmail.com.mysocialnetwork.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import java.util.*

@Entity(tableName = "user_table")
data class User(
//    val email:String,
    @PrimaryKey val id:UUID = UUID.randomUUID(),
    @SerializedName("username") val name:String?=null,
    @SerializedName("password") val password:String?=null,
    var userLoginStatus:LoginUserStatus

)

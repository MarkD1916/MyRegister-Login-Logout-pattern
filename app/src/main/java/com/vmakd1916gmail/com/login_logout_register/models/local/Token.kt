package com.vmakd1916gmail.com.login_logout_register.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = "token_table"
)
data class Token(
    @PrimaryKey val token_id: UUID = UUID.randomUUID(),
    @SerializedName("refresh") val refresh_token: String?="",
    @SerializedName("access") val access_token: String?=""
)

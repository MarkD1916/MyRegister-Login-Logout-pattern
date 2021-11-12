package com.vmakd1916gmail.com.mysocialnetwork.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = "token_table"
)
data class Token(
    @PrimaryKey val token_id: UUID = UUID.randomUUID(),
    @SerializedName("refresh") val refresh_token: String,
    @SerializedName("access") val access_token: String
)

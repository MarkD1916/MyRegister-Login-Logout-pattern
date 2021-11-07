package com.vmakd1916gmail.com.mysocialnetwork.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Token(
    @PrimaryKey val token_id: UUID = UUID.randomUUID(),
    val user_id:UUID?=null,
    @SerializedName("refresh") val refresh_token: String,
    @SerializedName("access") val access_token: String
)

data class UserAndToken(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val token: List<Token>
)
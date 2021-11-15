package com.vmakd1916gmail.com.login_logout_register.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val userID:UUID,
    val userName:String?
)

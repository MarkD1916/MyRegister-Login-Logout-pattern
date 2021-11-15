package com.vmakd1916gmail.com.login_logout_register.DB

import androidx.room.TypeConverter
import java.util.*

class TypeConverter {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}
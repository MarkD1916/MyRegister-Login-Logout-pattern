package com.vmakd1916gmail.com.login_logout_register.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vmakd1916gmail.com.login_logout_register.models.Token
import com.vmakd1916gmail.com.login_logout_register.models.local.User

@Database(entities = [User::class, Token::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class MySocialNetworkDatabase : RoomDatabase() {
    abstract fun mySocialNetworkDAO(): MySocialNetworkDAO
}
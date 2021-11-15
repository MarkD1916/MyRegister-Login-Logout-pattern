package com.vmakd1916gmail.com.login_logout_register.DB


import androidx.lifecycle.LiveData
import androidx.room.*
import com.vmakd1916gmail.com.login_logout_register.models.Token


@Dao
interface MySocialNetworkDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(vararg token: Token?)

    @Query("SELECT * FROM token_table")
    fun getToken():LiveData<Token>

    @Query("UPDATE token_table SET access_token=:accessToken")
    fun updateAccessToken(accessToken:String)

    @Delete
    fun deleteToken(token:Token)
}
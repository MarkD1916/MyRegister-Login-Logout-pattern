package com.vmakd1916gmail.com.mysocialnetwork.DB


import androidx.lifecycle.LiveData
import androidx.room.*
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import retrofit2.http.DELETE
import java.util.*


@Dao
interface MySocialNetworkDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(vararg token: Token?)

    @Query("SELECT * FROM token_table")
    fun getToken():LiveData<Token>

    @Delete
    fun deleteToken(token:Token)
}
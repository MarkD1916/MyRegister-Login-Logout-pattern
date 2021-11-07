package com.vmakd1916gmail.com.mysocialnetwork.DB


import androidx.lifecycle.LiveData
import androidx.room.*
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.UserAndToken
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.RegisterStatus
import java.util.*


@Dao
interface MySocialNetworkDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: User?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(vararg token: Token?)

    @Query("SELECT * FROM user_table")
    fun getUserFromDB(): LiveData<List<User>>


    @Transaction
    @Query("SELECT * FROM user_table WHERE id = :user_id")
    fun getTokenByUserId(user_id: UUID): LiveData<List<UserAndToken>>

    @Query("SELECT * FROM user_table WHERE userLoginStatus = :loginStatus")
    fun getCurrentAuthUser(loginStatus: LoginUserStatus):LiveData<User>

    @Query("UPDATE user_table SET userLoginStatus = :loginStatus WHERE id=(:user_id)")
    fun updateLoginStatus(loginStatus: LoginUserStatus,user_id: UUID)

    @Delete
    suspend fun deleteToken(vararg token: Token)
}
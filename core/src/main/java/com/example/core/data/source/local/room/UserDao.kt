package com.example.core.data.source.local.room

import androidx.room.*
import com.example.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun fetchUserFavorite(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserFavorite(user: UserEntity)

    @Delete
    suspend fun deleteUserFavorite(user: UserEntity)

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    fun getFavoriteIsExists(username: String): Flow<Boolean>
}
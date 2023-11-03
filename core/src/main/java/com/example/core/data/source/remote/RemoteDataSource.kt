package com.example.core.data.source.remote

import android.util.Log
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.ApiService
import com.example.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(
    private val apiService: ApiService,
) {

    suspend fun fetchUsersByUsername(username: String): Flow<ApiResponse<List<UserResponse>>>
            = flow {
        val response = try {
            apiService.fetchUserByUsername(username)
        } catch (e: Exception) {
            emit(ApiResponse.MyError(e.toString()))
            return@flow
        }

        if (response.items.isNotEmpty()) {
            emit(ApiResponse.MySuccess(response.items))
        } else {
            emit(ApiResponse.MyEmpty)
        }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchUserDetail(username: String): Flow<ApiResponse<UserResponse>> {
        return flow {
            val response = try {
                apiService.fetchUserDetail(username)
            } catch (e: Exception) {
                null
            }

            if (response != null) {
                emit(ApiResponse.MySuccess(response))
            } else {
                emit(ApiResponse.MyError("Failed to fetch user detail"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchUserFollowers(username: String): Flow<ApiResponse<List<UserResponse>>> {
        return flow {
            try {
                val response = apiService.fetchUserFollowers(username)
                if (response.isNotEmpty()) {
                    emit(ApiResponse.MySuccess(response))
                } else {
                    emit(ApiResponse.MyEmpty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.MyError(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun fetchUserFollowing(username: String): Flow<ApiResponse<List<UserResponse>>> {
        return flow {
            try {
                val response = apiService.fetchUserFollowing(username)
                if (response.isNotEmpty()) {
                    emit(ApiResponse.MySuccess(response))
                } else {
                    emit(ApiResponse.MyEmpty)
                }
            } catch (e: Exception){
                emit (ApiResponse.MyError(e.toString()))
                Log.e("RemoteDataSOurce", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
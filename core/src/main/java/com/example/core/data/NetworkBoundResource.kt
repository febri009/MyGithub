package com.example.core.data

import com.example.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = createNetworkRequest()

    private fun createNetworkRequest(): Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val apiResponse = createCall().first()
        when (apiResponse) {
            is ApiResponse.MySuccess -> {
                val data = apiResponse.data
                val resource = loadFromNetwork(data).map { Resource.Success(it) }
                emitAll(resource)
            }
            is ApiResponse.MyEmpty -> {
                Resource.Success(null)
            }
            is ApiResponse.MyError -> {
                emit(Resource.Error(apiResponse.errorMessage))
            }
        }
    }

    protected abstract fun loadFromNetwork(data: RequestType): Flow<ResultType>

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    fun asFlow(): Flow<Resource<ResultType>> = result
}
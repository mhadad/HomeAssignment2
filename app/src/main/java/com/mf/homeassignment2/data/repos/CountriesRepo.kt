package com.mf.homeassignment2.data.repos

import com.mf.homeassignment2.data.dataSources.remoteDataSource.API_Service
import com.mf.homeassignment2.data.models.Country_Model
import com.mf.homeassignment2.data.models.DataStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountriesRepo(private val apiService:API_Service) {
    suspend fun execGetCountries(): Flow<DataStates.Success<List<Country_Model>?>> {
        val countriesResp = apiService.getCountries()
        if (countriesResp.isSuccessful) {
            return flow {
                emit(DataStates.Success(countriesResp.body() ?: emptyList()))
            }
        } else if (countriesResp.errorBody() != null) {
            throw setError(
                countriesResp.errorBody()?.string()
                    ?: "Network error has occurred"
            )
        } else {
            throw IllegalStateException("Network error has occurred")
        }
    }
    private fun setError(message: String): Throwable{
        return Throwable(message)
    }
}
package com.mf.homeassignment2.data.dataSources.remoteDataSource

import com.mf.homeassignment2.data.models.Country_Model
import retrofit2.Response
import retrofit2.http.GET

interface API_Service {
    @GET("peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
    suspend fun getCountries(): Response<List<Country_Model>?>
}
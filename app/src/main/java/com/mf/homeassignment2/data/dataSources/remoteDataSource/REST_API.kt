package com.mf.homeassignment2.data.dataSources.remoteDataSource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class REST_API(val baseUrl: String) {
        fun getAPIServiceInstance(): API_Service{
            return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create<API_Service>(API_Service::class.java)
        }
}
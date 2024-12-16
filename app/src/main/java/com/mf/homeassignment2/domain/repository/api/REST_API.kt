package com.mf.homeassignment2.domain.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object REST_API {
        fun getAPIServiceInstance(baseUrl: String): API_Service{
            return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create<API_Service>(API_Service::class.java)
        }
}
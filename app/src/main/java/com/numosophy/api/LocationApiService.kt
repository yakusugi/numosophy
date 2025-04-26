package com.numosophy.api
import retrofit2.http.GET
import com.numosophy.model.CountryResponse

interface LocationApiService {
    @GET("v0.1/countries/states")
    suspend fun getCountriesAndStates(): CountryResponse
}

annotation class GET(val value: String)

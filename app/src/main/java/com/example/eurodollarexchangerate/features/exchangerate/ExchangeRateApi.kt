
package com.example.eurodollarexchangerate.features.exchangerate

import com.example.eurodollarexchangerate.features.exchangerate.entity.RatesEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface ExchangeRateApi {

    @GET("/history")
    fun getProgramsFromNetwork(@QueryMap params: Map<String, String>): Call<RatesEntity>
}

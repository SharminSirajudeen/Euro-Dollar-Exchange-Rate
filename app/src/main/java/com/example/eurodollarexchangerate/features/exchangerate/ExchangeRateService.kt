package com.example.eurodollarexchangerate.features.exchangerate

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateService
@Inject constructor(retrofit: Retrofit) : ExchangeRateApi {
    private val exchangeRateApi by lazy { retrofit.create(ExchangeRateApi::class.java) }
    override fun getProgramsFromNetwork(params: Map<String, String>) = exchangeRateApi.getProgramsFromNetwork(params)
}

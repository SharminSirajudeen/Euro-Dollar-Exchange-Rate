package com.example.eurodollarexchangerate.features.exchangerate.entity

import com.example.eurodollarexchangerate.core.extension.empty
import com.example.eurodollarexchangerate.features.exchangerate.model.Rates
import com.example.eurodollarexchangerate.features.exchangerate.model.TargetCurrency
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesEntity(
    @SerializedName("base") @Expose var baseCurrency: String,
    @SerializedName("rates") @Expose var rates: Map<String, TargetCurrency>,
    @SerializedName("end_at") @Expose var endDate: String,
    @SerializedName("start_at") @Expose var startDate: String
) {
    companion object {
        fun empty() = RatesEntity(String.empty(), mapOf(), String.empty(), String.empty())
    }

    fun toRates() = Rates(baseCurrency, rates, endDate, startDate)
}
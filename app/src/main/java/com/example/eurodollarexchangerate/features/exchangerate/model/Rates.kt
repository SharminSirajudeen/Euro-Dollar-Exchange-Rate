package com.example.eurodollarexchangerate.features.exchangerate.model

import com.example.eurodollarexchangerate.core.extension.empty

data class Rates(
    var baseCurrency: String,
    var rates: Map<String, TargetCurrency>,
    var endDate: String,
    var startDate: String
) {

    companion object {
        fun empty() = Rates(String.empty(), mapOf(), String.empty(), String.empty())
    }
}

package com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist

import com.example.eurodollarexchangerate.features.exchangerate.model.TargetCurrency

data class RatesView(var baseCurrency: String,
                     var rates: Map<String, TargetCurrency>,
                     var endDate: String,
                     var startDate: String)

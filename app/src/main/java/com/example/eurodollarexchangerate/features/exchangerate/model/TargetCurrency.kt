package com.example.eurodollarexchangerate.features.exchangerate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TargetCurrency(@SerializedName("USD") @Expose var usd: Double)
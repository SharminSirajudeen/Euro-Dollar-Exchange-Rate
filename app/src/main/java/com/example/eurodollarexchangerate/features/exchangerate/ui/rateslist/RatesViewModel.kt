package com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.example.eurodollarexchangerate.core.platform.BaseViewModel
import com.example.eurodollarexchangerate.features.exchangerate.model.Rates
import com.example.eurodollarexchangerate.features.exchangerate.usecase.GetRatesList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class RatesViewModel
@Inject constructor(private val getRatesList: GetRatesList) : BaseViewModel() {
    var exchangeRates: MutableLiveData<ArrayList<Pair<Date, Double>>> = MutableLiveData()

    fun loadRates(start: String, end: String) = getRatesList(GetRatesList.Params(startDate = start, endDate = end)) {
        it.either(
            ::handleFailure,
            ::handleSuccess
        )
    }

    private fun handleSuccess(rates: Rates) {
        val dates = ArrayList<Pair<Date, Double>>()
        for (rate in rates.rates) {
            try {
                val dateObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rate.key)
                dates.add(Pair(first = dateObj, second = rate.value.usd))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dates.sortWith(Comparator { o1, o2 -> o1.first.compareTo(o2.first) })

        }
        this.exchangeRates.postValue(dates)
    }

}
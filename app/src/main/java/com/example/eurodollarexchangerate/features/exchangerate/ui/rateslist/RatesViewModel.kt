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
@Inject constructor(private val getRatesList: GetRatesList, private val context: Context) : BaseViewModel() {

    //This is an application context, so it will not leak
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
            Collections.sort(dates, object : Comparator<Pair<Date, Double>> {
                override fun compare(o1: Pair<Date, Double>, o2: Pair<Date, Double>): Int {
                    return o1.first.compareTo(o2.first)
                }
            })

        }
        saveData(dates)
        this.exchangeRates.postValue(dates)
    }

    private fun saveData(dates: ArrayList<Pair<Date, Double>>) {
        var sharedPreferences = context.getSharedPreferences("EXCHANGE_RATE", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(dates)
        val editor = sharedPreferences.edit()
        editor.putString("Set", json)

        val savingTime = Date()
        var stringDate = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).format(savingTime)
        editor.putString("timestamp", stringDate)
        editor.apply()
    }

    fun getRatesForDate(start: String, end: String) {
        var sharedPreferences = context.getSharedPreferences("EXCHANGE_RATE", Context.MODE_PRIVATE)
        var json = sharedPreferences.getString("Set", "")
        var timeStamp = sharedPreferences.getString("timestamp", "")
        var ratesList = ArrayList<Pair<Date, Double>>()
        if (json.isNotEmpty()) {
            ratesList = Gson().fromJson<ArrayList<Pair<Date, Double>>>(
                json,
                object : TypeToken<ArrayList<Pair<Date, Double>>>() {}.type
            )
                ?: ArrayList()
        }


        if (ratesList.isNotEmpty() && !dataExpired(
                SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).parse(
                    timeStamp
                )
            )
        ) {
            this.exchangeRates.postValue(ratesList)
        } else {
            loadRates(start = start, end = end)
        }
    }

    private fun dataExpired(timeStamp: Date): Boolean {
        try {
            var currentTime = Date()
            val difference = currentTime.time - timeStamp.time
            return difference / (24 * 60 * 60 * 1000) >= 1
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }
    }
}
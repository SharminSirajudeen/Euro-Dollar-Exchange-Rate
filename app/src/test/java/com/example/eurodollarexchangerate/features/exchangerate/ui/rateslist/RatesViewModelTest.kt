package com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist

import com.example.eurodollarexchangerate.AndroidTest
import com.example.eurodollarexchangerate.core.functional.Either.Right
import com.example.eurodollarexchangerate.features.exchangerate.model.Rates
import com.example.eurodollarexchangerate.features.exchangerate.model.TargetCurrency
import com.example.eurodollarexchangerate.features.exchangerate.usecase.GetRatesList
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import java.text.SimpleDateFormat
import java.util.*

class RatesViewModelTest : AndroidTest() {

    private lateinit var ratesViewModel: RatesViewModel
    @Mock
    private lateinit var getRatesList: GetRatesList

    @Before
    fun setUp() {
        ratesViewModel = RatesViewModel(getRatesList)
    }

    @Test
    fun `loading rates should update live data`() {
        var rates = Rates("eur", mapOf(Pair("2010-12-07", TargetCurrency(1.3363))), "2010-12-07", "2010-12-07")

        given { runBlocking { getRatesList.run(eq(any())) } }.willReturn(Right(rates))

        ratesViewModel.exchangeRates.observeForever {
            with(it!!) {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.get(0).first) shouldEqualTo "2010-12-07"
                it.get(0).second shouldEqualTo 1.3363
            }
        }

        runBlocking { ratesViewModel.loadRates("2010-12-07", "2010-12-07") }
    }
}
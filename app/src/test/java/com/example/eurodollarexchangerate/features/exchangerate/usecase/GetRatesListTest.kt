package com.example.eurodollarexchangerate.features.exchangerate.usecase

import com.example.eurodollarexchangerate.UnitTest
import com.example.eurodollarexchangerate.core.extension.empty
import com.example.eurodollarexchangerate.core.functional.Either.Right
import com.example.eurodollarexchangerate.features.exchangerate.RatesRepository
import com.example.eurodollarexchangerate.features.exchangerate.model.Rates
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetRatesListTest : UnitTest() {
    private lateinit var getRatesList: GetRatesList

    @Mock
    private lateinit var ratesRepository: RatesRepository

    @Before
    fun setUp() {
        getRatesList = GetRatesList(ratesRepository)
        given {
            ratesRepository.getRates(
                startDate = String.empty(),
                endDate = String.empty()
            )
        }.willReturn(Right(Rates.empty()))
    }

    @Test
    fun `should get data from repository`() {
        runBlocking { getRatesList.run(GetRatesList.Params(startDate = String.empty(), endDate = String.empty())) }

        verify(ratesRepository).getRates(startDate = String.empty(), endDate = String.empty())
        verifyNoMoreInteractions(ratesRepository)
    }
}

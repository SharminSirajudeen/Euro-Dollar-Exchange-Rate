package com.example.eurodollarexchangerate.features.exchangerate

import com.example.eurodollarexchangerate.UnitTest
import com.example.eurodollarexchangerate.core.exception.Failure
import com.example.eurodollarexchangerate.core.functional.Either
import com.example.eurodollarexchangerate.core.platform.NetworkHandler
import com.example.eurodollarexchangerate.features.exchangerate.entity.RatesEntity
import com.example.eurodollarexchangerate.features.exchangerate.model.TargetCurrency
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response

class RatesRepositoryTest : UnitTest() {

    private lateinit var networkRepository: RatesRepository.Network

    @Mock
    private lateinit var networkHandler: NetworkHandler
    @Mock
    private lateinit var service: ExchangeRateService

    @Mock
    private lateinit var ratesCall: Call<RatesEntity>
    @Mock
    private lateinit var ratesResponse: Response<RatesEntity>

    @Before
    fun setUp() {
        networkRepository = RatesRepository.Network(networkHandler, service)
    }

    private fun getDateRange(startDate: String, endDate: String): HashMap<String, String> {
        val data = HashMap<String, String>()
        data.put("start_at", startDate)
        data.put("end_at", endDate)
        return data
    }

    @Test
    fun `should return empty list by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { ratesResponse.body() }.willReturn(null)
        given { ratesResponse.isSuccessful }.willReturn(true)
        given { ratesCall.execute() }.willReturn(ratesResponse)
        given { service.getProgramsFromNetwork(mapOf()) }.willReturn(ratesCall)

        val ratesEntity = networkRepository.getRates("", "")

        ratesEntity shouldEqual Either.Right(emptyList<RatesEntity>())
        verify(service).getProgramsFromNetwork(mapOf())
    }

    @Test
    fun `should get exchange rates from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { ratesResponse.body() }.willReturn(
            RatesEntity(
                "eur",
                mapOf(Pair("2010-12-07", TargetCurrency(1.3363))),
                "2010-12-07",
                "2010-12-07"
            )
        )
        given { ratesResponse.isSuccessful }.willReturn(true)
        given { ratesCall.execute() }.willReturn(ratesResponse)
        given { service.getProgramsFromNetwork(getDateRange("2010-12-07", "2010-12-07")) }.willReturn(ratesCall)

        val ratesEntity = networkRepository.getRates("2010-12-07", "2010-12-07")

        ratesEntity shouldEqual Either.Right(
            RatesEntity(
                "eur",
                mapOf(Pair("2010-12-07", TargetCurrency(1.3363))),
                "2010-12-07",
                "2010-12-07"
            )
        )
        verify(service).getProgramsFromNetwork(getDateRange("2010-12-07", "2010-12-07"))
    }

    @Test
    fun `movies service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val ratesEntity = networkRepository.getRates("2010-12-07", "2010-12-07")

        ratesEntity shouldBeInstanceOf Either::class.java
        ratesEntity.isLeft shouldEqual true
        ratesEntity.either({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val ratesEntity = networkRepository.getRates("2010-12-07", "2010-12-07")

        ratesEntity shouldBeInstanceOf Either::class.java
        ratesEntity.isLeft shouldEqual true
        ratesEntity.either({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val ratesEntity = networkRepository.getRates("2010-12-07", "2010-12-07")

        ratesEntity shouldBeInstanceOf Either::class.java
        ratesEntity.isLeft shouldEqual true
        ratesEntity.either({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})

    }

    @Test
    fun `movies request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val ratesEntity = networkRepository.getRates("2010-12-07", "2010-12-07")

        ratesEntity shouldBeInstanceOf Either::class.java
        ratesEntity.isLeft shouldEqual true
        ratesEntity.either({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

}
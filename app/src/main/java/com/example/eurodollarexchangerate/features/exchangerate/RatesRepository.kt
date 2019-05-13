
package com.example.eurodollarexchangerate.features.exchangerate

import com.example.eurodollarexchangerate.core.exception.Failure
import com.example.eurodollarexchangerate.core.exception.Failure.NetworkConnection
import com.example.eurodollarexchangerate.core.exception.Failure.ServerError
import com.example.eurodollarexchangerate.core.functional.Either
import com.example.eurodollarexchangerate.core.functional.Either.Left
import com.example.eurodollarexchangerate.core.functional.Either.Right
import com.example.eurodollarexchangerate.core.platform.NetworkHandler
import com.example.eurodollarexchangerate.features.exchangerate.entity.RatesEntity
import com.example.eurodollarexchangerate.features.exchangerate.model.Rates
import retrofit2.Call
import javax.inject.Inject

interface RatesRepository {
    fun getRates(startDate: String, endDate: String): Either<Failure, Rates>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: ExchangeRateService) : RatesRepository {
        override fun getRates(startDate: String, endDate: String): Either<Failure, Rates> {
            return when (networkHandler.isConnected) {
                true -> request(service.getProgramsFromNetwork(getDateRange(startDate = startDate, endDate = endDate)), { it.toRates() }, RatesEntity.empty())
                false, null -> Left(NetworkConnection)
            }
        }

        private fun getDateRange(startDate: String, endDate: String): HashMap<String, String> {
            val data = HashMap<String, String>()
            data.put("start_at", startDate)
            data.put("end_at", endDate)
            return data
        }


        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(ServerError)
                }
            } catch (exception: Throwable) {
                Left(ServerError)
            }
        }
    }
}

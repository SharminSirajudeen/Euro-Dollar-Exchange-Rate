
package com.example.eurodollarexchangerate.features.exchangerate.usecase

import com.example.eurodollarexchangerate.core.interactor.UseCase
import com.example.eurodollarexchangerate.features.exchangerate.RatesRepository
import com.example.eurodollarexchangerate.features.exchangerate.model.Rates
import javax.inject.Inject

class GetRatesList
@Inject constructor(private val ratesRepository: RatesRepository) : UseCase<Rates, GetRatesList.Params>() {

    override suspend fun run(params: Params) = ratesRepository.getRates(params.startDate, params.endDate)

    data class Params(val startDate: String, val endDate: String)
}

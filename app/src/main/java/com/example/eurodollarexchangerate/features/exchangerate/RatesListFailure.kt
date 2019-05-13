
package com.example.eurodollarexchangerate.features.exchangerate

import com.example.eurodollarexchangerate.core.exception.Failure.FeatureFailure

class RatesListFailure {
    class ListNotAvailable : FeatureFailure()
}


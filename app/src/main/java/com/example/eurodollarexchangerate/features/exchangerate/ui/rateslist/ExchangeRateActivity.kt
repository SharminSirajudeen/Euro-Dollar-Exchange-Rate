package com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist

import android.content.Context
import android.content.Intent
import com.example.eurodollarexchangerate.core.platform.BaseActivity

class ExchangeRateActivity : BaseActivity() {
    override fun fragment() = RatesFragment()

    companion object {
        fun callingIntent(context: Context) = Intent(context, ExchangeRateActivity::class.java)
    }
}

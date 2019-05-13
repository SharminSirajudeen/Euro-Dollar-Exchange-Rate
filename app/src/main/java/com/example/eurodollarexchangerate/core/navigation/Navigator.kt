package com.example.eurodollarexchangerate.core.navigation

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist.ExchangeRateActivity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Navigator @Inject constructor() {
    private var mDelayHandler: Handler? = null
    private val DELAY: Long = 1000
    fun showMain(context: Context) {
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(Runnable {
            showGraphScreen(context)
        }, DELAY)

    }

    private fun showGraphScreen(context: Context) {
        (context as Activity).finish()
        context.startActivity(ExchangeRateActivity.callingIntent(context))
    }

    class Extras(val transitionSharedElement: View)
}



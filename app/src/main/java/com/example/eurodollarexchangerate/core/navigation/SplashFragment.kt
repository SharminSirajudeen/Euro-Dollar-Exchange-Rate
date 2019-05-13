package com.example.eurodollarexchangerate.core.navigation

import com.example.eurodollarexchangerate.R
import com.example.eurodollarexchangerate.core.extension.close
import com.example.eurodollarexchangerate.core.platform.BaseFragment

class SplashFragment : BaseFragment() {
    override fun layoutId()  = R.layout.fragment_splash

    override fun onStop() {
        super.onStop()
        close()
    }
}

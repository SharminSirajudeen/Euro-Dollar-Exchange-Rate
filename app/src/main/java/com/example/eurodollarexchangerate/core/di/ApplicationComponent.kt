package com.example.eurodollarexchangerate.core.di

import com.example.eurodollarexchangerate.AndroidApplication
import com.example.eurodollarexchangerate.core.di.viewmodel.ViewModelModule
import com.example.eurodollarexchangerate.core.navigation.SplashActivity
import com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist.RatesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(splashActivity: SplashActivity)

    fun inject(ratesFragment: RatesFragment)
}

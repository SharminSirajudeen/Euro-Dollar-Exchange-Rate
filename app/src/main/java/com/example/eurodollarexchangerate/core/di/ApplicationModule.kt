package com.example.eurodollarexchangerate.core.di

import android.content.Context
import android.util.Log
import com.example.eurodollarexchangerate.AndroidApplication
import com.example.eurodollarexchangerate.BuildConfig
import com.example.eurodollarexchangerate.features.exchangerate.RatesRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Log.d(
                    "APICalls",
                    message
                )
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }


    @Provides
    @Singleton
    fun provideRatesRepository(dataSource: RatesRepository.Network): RatesRepository = dataSource
}

package com.example.eurodollarexchangerate.core.navigation

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.eurodollarexchangerate.AndroidApplication
import com.example.eurodollarexchangerate.R
import com.example.eurodollarexchangerate.core.di.ApplicationComponent
import com.example.eurodollarexchangerate.core.platform.BaseActivity
import com.example.eurodollarexchangerate.core.platform.BaseFragment
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    @Inject
    internal lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)
        appComponent.inject(this)
        navigator.showMain(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun hideSystemUI() {
        lateinit var decorView: View;
        if (window != null) {
            decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
}

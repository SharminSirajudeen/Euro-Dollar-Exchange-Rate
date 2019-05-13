package com.example.eurodollarexchangerate.core.platform

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.eurodollarexchangerate.R.id
import com.example.eurodollarexchangerate.R.layout
import com.example.eurodollarexchangerate.core.extension.inTransaction

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(layout.activity_main)
        addFragment(savedInstanceState)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(id.fragmentContainer, fragment())
        }

    abstract fun fragment(): BaseFragment
}

/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.eurodollarexchangerate.core.navigation

import com.example.eurodollarexchangerate.AndroidTest
import com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist.ExchangeRateActivity
import com.example.eurodollarexchangerate.shouldNavigateTo
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
class NavigatorTest : AndroidTest() {

    private lateinit var navigator: Navigator

    @Before
    fun setup() {
        navigator = Navigator()
    }

    @Test
    fun `should forward user to rates screen`() {
        navigator.showMain(activityContext())
        SplashActivity::class shouldNavigateTo ExchangeRateActivity::class
    }
}

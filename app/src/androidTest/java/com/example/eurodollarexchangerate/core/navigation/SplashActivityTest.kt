package com.example.eurodollarexchangerate.core.navigation


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.example.eurodollarexchangerate.R
import com.example.eurodollarexchangerate.ViewShownIdlingResource
import com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist.ExchangeRateActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ExchangeRateActivity::class.java)

    /*@Before
    fun setup() {
        mActivityTestRule.launchActivity(null)
    }*/

    @Test
    fun splashActivityTest() {
        val viewGroup = onView(
            allOf(
                withId(R.id.rate_chart),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragmentContainer),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        waitViewShown(allOf(
            withId(R.id.rate_chart),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragmentContainer),
                    0
                ),
                0
            ),
            isDisplayed()
        ))

        viewGroup.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun waitViewShown(matcher: Matcher<View>) {
        val idlingResource = ViewShownIdlingResource(matcher)///
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            onView(matcher).check(matches(isDisplayed()))
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }
}

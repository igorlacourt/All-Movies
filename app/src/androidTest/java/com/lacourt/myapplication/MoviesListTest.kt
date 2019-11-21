package com.lacourt.myapplication
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lacourt.myapplication.indlingresource.IdlingResourceManager
import com.lacourt.myapplication.ui.home.MovieViewHolder
import com.lacourt.myapplication.ui.home.HomeFragment
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Test class showcasing some {@link RecyclerViewActions} from Espresso.
 */
@RunWith(AndroidJUnit4::class)
class MoviesListTest {

    private val ITEM_BELOW_THE_FOLD = 40
    private var mIdlingResource: IdlingResource? = null
    /**
     * Use [ActivityScenario] to create and launch the activity under test. This is a
     * replacement for [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule//@Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun init() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { homeFragment ->
            mIdlingResource = IdlingResourceManager.getIdlingResource()
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }

    @Test
    fun recyclerviewIsDisplayed() {
        Thread.sleep(10000)
        onView(withId(R.id.movie_list))
            .check(matches(isDisplayed()))
    }
//
//
//    @Test
//    fun scrollToItemBelowFold_checkItsText() {
//        // First scroll to the position that needs to be matched and click on it.
//        onView(withId(R.id.movie_list))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<MovieViewHolder>(
//                    0,
//                    click()
//                )
//            )
//    }

}
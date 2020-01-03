package com.movies.allmovies

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.movies.allmovies.ui.home.HomeFragment
import com.movies.allmovies.ui.mylist.MyListHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {
    private var mIdlingResource: IdlingResource? = null

    @get:Rule//@Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private var mockNavController: NavController? = null

    @Before
    fun init() {
        // Create a mock NavController
        mockNavController = mock(NavController::class.java)

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<HomeFragment>()

        // Set the NavController property on the fragment
        titleScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
//            mIdlingResource = IdlingResourceManager.getIdlingResource()
//            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Test
    fun after_loading_fragment_MyList_is_displayed() {
        Thread.sleep(50000)
        onView(withId(R.id.movie_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun when_MyList_is_displayed_click_first_item() {
        Thread.sleep(3000)
        onView(withId(R.id.my_list_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MyListHolder>(
                    0,
                    click()
                )
            )
        // Verify that performing a click prompts the correct Navigation action
//        verify(mockNavController)?.navigate(R.id.action_detailsFragment_self)
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }

}
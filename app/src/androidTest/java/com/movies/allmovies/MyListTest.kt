package com.movies.allmovies

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.movies.allmovies.ui.mylist.MyListFragment
import com.movies.allmovies.ui.mylist.MyListHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


/**
 * Test class showcasing some {@link RecyclerViewActions} from Espresso.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MyListTest {

    private val ITEM_BELOW_THE_FOLD = 40
    private var mIdlingResource: IdlingResource? = null
    /**
     * Use [ActivityScenario] to create and launch the activity under test. This is a
     * replacement for [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule//@Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    private var mockNavController: NavController? = null

    @Before
    fun init() {
//        val scenario = launchFragmentInContainer<MyListFragment>()
//        val scenario = launchFragmentInContainer<HomeFragment>()
//        scenario.moveToState(Lifecycle.State.RESUMED)

//        scenario.onFragment {
//            mIdlingResource = IdlingResourceManager.getIdlingResource()
//            IdlingRegistry.getInstance().register(mIdlingResource)
//        }

        // Create a mock NavController
        mockNavController = mock(NavController::class.java)

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<MyListFragment>()

        // Set the NavController property on the fragment
        titleScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
    }

    @Test
    fun after_loading_fragment_MyList_is_displayed() {
        onView(withId(R.id.my_list_list))
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
//        if (mIdlingResource != null) {
//            IdlingRegistry.getInstance().unregister(mIdlingResource)
//        }
    }

}

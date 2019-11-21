package com.lacourt.myapplication

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lacourt.myapplication.ui.home.HomeFragment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.annotation.NonNull
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.action.ViewActions
import com.lacourt.myapplication.idlingresource.IdlingResourceManager
import com.lacourt.myapplication.ui.mylist.MyListFragment
import com.lacourt.myapplication.ui.mylist.MyListHolder
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


/**
 * Test class showcasing some {@link RecyclerViewActions} from Espresso.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
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
//        val scenario = launchFragmentInContainer<MyListFragment>()
//        val scenario = launchFragmentInContainer<HomeFragment>()
//        scenario.moveToState(Lifecycle.State.RESUMED)

//        scenario.onFragment {
//            mIdlingResource = IdlingResourceManager.getIdlingResource()
//            IdlingRegistry.getInstance().register(mIdlingResource)
//        }
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }

    @Test
    fun testNavigationToInGameScreen() {
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<MyListFragment>()

        // Set the NavController property on the fragment
        titleScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.my_list_list))
            .check(matches(isDisplayed()))

        onView(ViewMatchers.withId(R.id.my_list_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MyListHolder>(
                    1,
                    click()
                )
            )
        // Verify that performing a click prompts the correct Navigation action
        verify(mockNavController).navigate(R.id.action_detailsFragment_self)
    }

    @Test
    fun recyclerviewIsDisplayed() {
        Thread.sleep(30000)
        onView(withId(R.id.movie_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun myListRecyclerviewIsDisplayed() {
        onView(withId(R.id.my_list_list))
            .check(matches(isDisplayed()))
        val mockNavController = mock(NavController::class.java)
    }

    @Test
    fun myListRecyclerviewClick() {
        Thread.sleep(3000)
    }

    @Test
    fun matchRecyclerviewItemText() {
        onView(withId(R.id.movie_list))
            .check(matches(atPosition(0, hasDescendant(withText("Test Text")))))
    }

    @Test
    fun clickRecyclerview() {
        onView(withId(R.id.my_list_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MyListHolder>(0, click()))
    }

    @Test
    fun scrollToItemBelowFold_checkItsText() {
        // First scroll to the position that needs to be matched and click on it.
//        onView(withId(R.id.movie_list))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<MovieViewHolder>(
//                    0,
//                    click()
//                )
//            )


//        // Match the text in an item below the fold and check that it's displayed.
//        val result = tmdbApi.getMovies(AppConstants.LANGUAGE, 1).execute()
//
//        if(result.isSuccessful) {
//            val movieTitle = result.body()?.results?.get(0)?.title
////            assertThat(movieTitle, `is`("Hustlers"))
//            onView(withText(movieTitle)).check(matches(isDisplayed()))
//        } else {
//            AssertionFailedError()
//        }


    }
//
//    @Test
//    fun itemInMiddleOfList_hasSpecialText() {
//        // First, scroll to the view holder using the isInTheMiddle matcher.
//        onView(ViewMatchers.withId(R.id.movie_list))
//            .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()))
//
//        // Check that the item has the special text.
//        val middleElementText =
//            "The Day Shall Come"
//        onView(withText(middleElementText)).check(matches(isDisplayed()))
//    }

    /**
     * Matches the [MovieViewHolder]s in the middle of the list.
     */
//    private fun isInTheMiddle(): Matcher<MovieViewHolder> {
//        return object : TypeSafeMatcher<MovieViewHolder>() {
//            override fun matchesSafely(customHolder: MovieViewHolder): Boolean {
//                return customHolder.IsInTheMiddle
//            }
//
//            override fun describeTo(description: Description) {
//                description.appendText("item in the middle")
//            }
//        }
//    }

    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}

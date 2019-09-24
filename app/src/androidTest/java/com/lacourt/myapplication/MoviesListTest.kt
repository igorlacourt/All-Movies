package com.lacourt.myapplication
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lacourt.myapplication.ui.MovieViewHolder
import com.lacourt.myapplication.ui.home.HomeFragment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Test class showcasing some {@link RecyclerViewActions} from Espresso.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MoviesListTest {

    private val ITEM_BELOW_THE_FOLD = 40

    /**
     * Use [ActivityScenario] to create and launch the activity under test. This is a
     * replacement for [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule//@Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun init() {
        activityTestRule.getActivity()
            .getSupportFragmentManager().beginTransaction()
            .add(R.id.nav_host_fragment, HomeFragment())
    }

    @Test
    fun recyclerviewIsDisplayed() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.movie_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun scrollToItemBelowFold_checkItsText() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.movie_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MovieViewHolder>(
                    0,
                    click()
                )
            )


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

    @Test
    fun itemInMiddleOfList_hasSpecialText() {
        // First, scroll to the view holder using the isInTheMiddle matcher.
        onView(ViewMatchers.withId(R.id.movie_list))
            .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()))

        // Check that the item has the special text.
        val middleElementText =
            "The Day Shall Come"
        onView(withText(middleElementText)).check(matches(isDisplayed()))
    }

    /**
     * Matches the [MovieViewHolder]s in the middle of the list.
     */
    private fun isInTheMiddle(): Matcher<MovieViewHolder> {
        return object : TypeSafeMatcher<MovieViewHolder>() {
            override fun matchesSafely(customHolder: MovieViewHolder): Boolean {
                return customHolder.IsInTheMiddle
            }

            override fun describeTo(description: Description) {
                description.appendText("item in the middle")
            }
        }
    }
}
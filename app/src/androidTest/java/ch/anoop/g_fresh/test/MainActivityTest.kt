package ch.anoop.g_fresh.test

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view.activity.MainActivity
import ch.anoop.g_fresh.view.custom.CustomSearchView
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun beforeEachTest() {
        Intents.init()
    }

    @After
    fun afterEachTest() {
        Intents.release()
    }

    @Test
    fun tabClick001() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.CREATED)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.tabs)).perform(
            TabClickPositionAction(1)
        )

        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun tabViewPagerSwipe002() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.CREATED)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.view_pager)).perform(
            swipeLeft()
        )

        onView(withId(R.id.view_pager)).perform(
            swipeRight()
        )

        onView(withId(R.id.view_pager)).perform(
            swipeLeft()
        )

        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun search() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.CREATED)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        typeSearchViewText("").apply { }//- Empty  -- loads trending
        typeSearchViewText(" ") // blank
        typeSearchViewText("cat") // text

        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }


    private fun typeSearchViewText(text: String?): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): org.hamcrest.Matcher<View>? {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(
                    withId(R.id.giffy_search_view),
                    withEffectiveVisibility(Visibility.VISIBLE),
                    isDisplayed(),
                    isAssignableFrom(CustomSearchView::class.java)
                )
            }

            override fun getDescription(): String {
                return "description"
            }

            override fun perform(uiController: UiController, view: View) {
                val searchView = view as CustomSearchView
                searchView.isIconified = false
                searchView.setQuery(text, false)
            }
        }
    }
}


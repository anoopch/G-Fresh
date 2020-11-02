package ch.anoop.g_fresh.test

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import ch.anoop.g_fresh.R
import org.hamcrest.CoreMatchers

class QueryInProgressIdlingResource : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return QueryInProgressIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        return CoreMatchers.allOf(
            ViewMatchers.withId(R.id.giffy_loading_progress_bar),
            ViewMatchers.isCompletelyDisplayed(),
            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        ) != null
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

}
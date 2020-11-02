package ch.anoop.g_fresh.test

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher


class TabClickPositionAction(private val tabPosition: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "Click on tab"
    }

    override fun perform(uiController: UiController?, view: View) {
        if (view is TabLayout) {
            val tab = view.getTabAt(tabPosition)
            tab?.select()
        }
    }
}
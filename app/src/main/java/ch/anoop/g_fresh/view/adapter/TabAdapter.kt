package ch.anoop.g_fresh.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view.fragment.FavoriteFragment
import ch.anoop.g_fresh.view.fragment.TrendingSearchFragment

/**
 * Adapter for handling the tabs in the MainActivity
 *      -   Holds reference to the two tab fragments
 */
class TabAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val searchFragment = TrendingSearchFragment()
    private val favFragment = FavoriteFragment()

    override fun getItem(position: Int): Fragment {
        return if (position == 0) searchFragment else favFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(
            if (position == 0) R.string.tab_text_1 else R.string.tab_text_2
        )
    }

    override fun getCount(): Int {
        return 2
    }
}
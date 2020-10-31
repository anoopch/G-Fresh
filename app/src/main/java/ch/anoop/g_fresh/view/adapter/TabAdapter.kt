package ch.anoop.g_fresh.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view.fragment.FavoriteFragment
import ch.anoop.g_fresh.view.fragment.TrendingSearchFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class TabAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val searchFragment = TrendingSearchFragment()
    private val favFragment = FavoriteFragment()

    override fun getItem(position: Int): Fragment {
        return if (position == 0) searchFragment else favFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}
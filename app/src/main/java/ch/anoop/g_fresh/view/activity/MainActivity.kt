package ch.anoop.g_fresh.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view.adapter.TabAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Single Main Activity class
 *      -   Responsible for adding the Viewpager, Tab, title and icons for the tab
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTabsWithViewPager()
    }

    /**
     * Function to add connections between the tab layout, viewpager.
     *
     * Also sets the tab title and icon
     */
    private fun setupTabsWithViewPager() {
        view_pager.adapter = TabAdapter(this, supportFragmentManager)
        tabs.apply {
            setupWithViewPager(view_pager)
            getTabAt(0)?.setIcon(R.drawable.ic_trending)
            getTabAt(1)?.setIcon(R.drawable.ic_favorite)
        }
    }
}
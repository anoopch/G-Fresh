package ch.anoop.g_fresh.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout

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
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = TabAdapter(this, supportFragmentManager)

        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_trending)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_favorite)
    }
}
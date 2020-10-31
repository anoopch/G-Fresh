package ch.anoop.g_fresh.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTabsWithViewPager()
    }

    private fun setupTabsWithViewPager() {
        val sectionsPagerAdapter = TabAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)


        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_trending)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_favorite)
    }
}
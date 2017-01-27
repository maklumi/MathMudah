package com.yahoo.maklumi.mathmudah

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

//todo highscore, setting difficulty,


class MainActivity : AppCompatActivity() {

    var f: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setLogo(R.mipmap.ic_launcher)

        fragmentManager.beginTransaction()
                .add(R.id.fragment, MainActivityFragment(), "fragment_visible")
                .commit()

        fragmentManager.addOnBackStackChangedListener {
            f = toolbar.menu.getItem(0)
            val currentBackStackFragment = fragmentManager.findFragmentByTag("fragment_visible")

            if (currentBackStackFragment is MainActivityFragment) {
                Log.d(TAG, "backstack is $currentBackStackFragment")
                f?.isVisible = true
                toolbar.setLogo(R.mipmap.ic_launcher)
                toolbar.navigationIcon = null
                toolbar.setTitle(R.string.app_label)
            } else if (currentBackStackFragment is SettingsFragment) {
                Log.d(TAG, "backstack is now $currentBackStackFragment")
                f?.isVisible = false
                toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
                toolbar.logo = null
                toolbar.title = null
                toolbar.setNavigationOnClickListener { fragmentManager.popBackStack() }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.first_item) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, SettingsFragment(), "fragment_visible")
                    .addToBackStack("fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()

            return true
        }

        return super.onOptionsItemSelected(item)
    }


    val TAG: String = MainActivity::class.java.simpleName
}

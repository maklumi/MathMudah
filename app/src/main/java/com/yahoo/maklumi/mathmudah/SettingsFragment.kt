package com.yahoo.maklumi.mathmudah

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.Menu
import android.view.MenuItem

/**
 * Created by HomePC on 26/1/2017.
 */
class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)


    }

    companion object {

        val KEY_MAIN_MUZIK_LATAR = "bunyi_latar"
    }

    val TAG = SettingsFragment::class.java.simpleName

}
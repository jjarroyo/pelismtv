package com.jj.pelismtv.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.jj.pelismtv.R
import com.jj.pelismtv.ui.onboarding.OnboardingActivity
import com.jj.pelismtv.ui.onboarding.OnboardingFragment
import com.jj.pelismtv.ui.search.LeanbackActivity

/**
 * Loads [MainFragment].
 */
class MainActivity : LeanbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (!sharedPreferences.getBoolean(OnboardingFragment.COMPLETED_ONBOARDING, false)) {
            startActivity(Intent(this, OnboardingActivity::class.java))

        }

    }
}
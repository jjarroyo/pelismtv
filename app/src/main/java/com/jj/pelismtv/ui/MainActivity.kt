package com.jj.pelismtv.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.FragmentActivity
import com.jj.pelismtv.R
import com.jj.pelismtv.ui.onboarding.OnboardingActivity
import com.jj.pelismtv.ui.onboarding.OnboardingFragment

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (!sharedPreferences.getBoolean(OnboardingFragment.COMPLETED_ONBOARDING, false)) {
            startActivity(Intent(this, OnboardingActivity::class.java))

        }

    }
}
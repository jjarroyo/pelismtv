package com.jj.pelismtv.ui.search

import android.content.Intent
import androidx.fragment.app.FragmentActivity

public abstract class LeanbackActivity: FragmentActivity() {

    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchActivity::class.java))
        return true
    }
}
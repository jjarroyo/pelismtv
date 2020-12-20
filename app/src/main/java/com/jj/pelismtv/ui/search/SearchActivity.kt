package com.jj.pelismtv.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.jj.pelismtv.R


class SearchActivity : LeanbackActivity() {
    private var mFragment: SearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)

        mFragment = supportFragmentManager
                .findFragmentById(R.id.search_fragment) as SearchFragment?
    }

    override fun onSearchRequested(): Boolean {
        if (mFragment?.hasResults() == true) {
            startActivity(Intent(this, SearchActivity::class.java))
        } else {
            mFragment!!.startRecognition()
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // If there are no results found, press the left key to reselect the microphone
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && !mFragment!!.hasResults()) {
            mFragment?.focusOnSearch()
        }
        return super.onKeyDown(keyCode, event)
    }

}
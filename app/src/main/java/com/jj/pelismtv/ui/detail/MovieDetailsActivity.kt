package com.jj.pelismtv.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jj.pelismtv.R
import com.jj.pelismtv.ui.search.LeanbackActivity

open class MovieDetailsActivity : LeanbackActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_movie_details)
    }



}
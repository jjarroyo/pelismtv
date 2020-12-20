package com.jj.pelismtv

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.jj.pelismtv.model.Movie

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
            viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
            item: Any) {
        val movie = item as Movie

        viewHolder.title.text = movie.title
        viewHolder.body.text = movie.overview
    }
}
package com.jj.pelismtv.presenter

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.utils.Common

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
            viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
            item: Any) {
        val movie = item as Movie

        viewHolder.title.text = movie.title
        viewHolder.body.text = Common.formatData(movie.overview?:"")
    }
}
package com.jj.pelismtv.presenter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.jj.pelismtv.R
import com.jj.pelismtv.model.Movie


class CardPresenter: Presenter() {

    private var mSelectedBackgroundColor = -1
    private var mDefaultBackgroundColor = -1
    private var mDefaultCardImage: Drawable? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        mDefaultBackgroundColor =
            ContextCompat.getColor(parent!!.context, R.color.default_background)
        mSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = parent.resources.getDrawable(R.drawable.movie, null)

        val cardView: ImageCardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) mSelectedBackgroundColor else mDefaultBackgroundColor

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color)
        view.findViewById<View>(R.id.info_field).setBackgroundColor(color)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val video: Movie = item as Movie

        val cardView = viewHolder!!.view as ImageCardView
        cardView.titleText = video.title
        cardView.contentText = video.overview

        // Set card size from dimension resources.
        val res = cardView.resources
        val width = res.getDimensionPixelSize(R.dimen.card_width)
        val height = res.getDimensionPixelSize(R.dimen.card_height)
        cardView.setMainImageDimensions(width, height)
        Glide.with(cardView.context)
            .load<Any>(video.poster_path)
            .error(mDefaultCardImage)
            .into(cardView.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        val cardView = viewHolder!!.view as ImageCardView

        // Remove references to images so that the garbage collector can free up memory.

        // Remove references to images so that the garbage collector can free up memory.
        cardView.badgeImage = null
        cardView.mainImage = null
    }
}
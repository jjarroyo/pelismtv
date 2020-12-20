package com.jj.pelismtv.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.jj.pelismtv.R

class IconHeaderItemPresenter: RowHeaderPresenter() {

    private var mUnselectedAlpha = 0f

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(viewGroup: ViewGroup): ViewHolder? {
        mUnselectedAlpha = viewGroup.resources
            .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1)
        val inflater = viewGroup.context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.icon_header_item, null)
        view.alpha = mUnselectedAlpha // Initialize icons to be at half-opacity.
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val headerItem = (item as ListRow).headerItem
        val rootView = viewHolder.view
        rootView.isFocusable = true
        val iconView = rootView.findViewById<View>(R.id.header_icon) as ImageView
        val icon = rootView.resources.getDrawable(R.drawable.android_header, null)
        iconView.setImageDrawable(icon)
        val label = rootView.findViewById<View>(R.id.header_label) as TextView
        label.text = headerItem.name
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {
        // no op
    }

    // TODO: This is a temporary fix. Remove me when leanback onCreateViewHolder no longer sets the
    // mUnselectAlpha, and also assumes the xml inflation will return a RowHeaderView.
    override fun onSelectLevelChanged(holder: ViewHolder) {
        holder.view.alpha = mUnselectedAlpha + holder.selectLevel *
                (1.0f - mUnselectedAlpha)
    }

}
package com.jj.pelismtv.ui

import android.R.attr
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.leanback.widget.TitleViewAdapter
import com.jj.pelismtv.R


class CustomTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TitleViewAdapter.Provider {


    private var mTitleView: TextView? = null
    private var seriesBtn: Button? = null
    private var mSearchOrbView: View? = null



    init {
       val root = View.inflate(context, R.layout.view_custom_title, this)
        mTitleView = root.findViewById(R.id.title_tv) as TextView
        seriesBtn = root.findViewById(R.id.serie_btn)
        mSearchOrbView = root.findViewById(R.id.search_orb)
        seriesBtn?.setOnClickListener {
            if(mTitleView?.text == "Peliculas"){
                val intent = Intent(context, SerieActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent, null)
            }else if(mTitleView?.text == "Series"){
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent, null)
            }
        }
    }

    private val titleViewAdapter = object : TitleViewAdapter() {

        override fun setTitle(titleText: CharSequence?) {
            this@CustomTitleView.setTitle(titleText)
        }

        override fun getSearchAffordanceView(): View? = null

        override fun setOnSearchClickedListener(listener: OnClickListener?) {
            mSearchOrbView?.setOnClickListener(listener)
        }


    }

    private fun setTitle(title: CharSequence?) {
        if(title == "Peliculas"){
            mTitleView?.text = title
            seriesBtn?.text = "Ver Series"
        }else if(title == "Series"){
            mTitleView?.text = title
            seriesBtn?.text = "Ver Peliculas"
        }
    }

    override fun getTitleViewAdapter(): TitleViewAdapter {
        return titleViewAdapter
    }


}
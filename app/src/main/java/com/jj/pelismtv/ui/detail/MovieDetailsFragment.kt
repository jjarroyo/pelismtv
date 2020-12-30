package com.jj.pelismtv.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.DetailsFragmentBackgroundController
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.jj.pelismtv.R
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.presenter.DetailsDescriptionPresenter
import com.jj.pelismtv.ui.MainViewModel
import com.jj.pelismtv.ui.MainViewModelFactory
import com.jj.pelismtv.ui.detail.player.PlayerActivity
import kotlin.math.roundToInt

class MovieDetailsFragment: DetailsSupportFragment() {

    private var mSelectedMovie: Movie? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var mDetailsBackground: DetailsFragmentBackgroundController
    private lateinit var mPresenterSelector: ClassPresenterSelector
    private lateinit var mAdapter: ArrayObjectAdapter
    private var mBackgroundManager: BackgroundManager? = null
    private var mDefaultBackground: Drawable? = null
    private var mMetrics: DisplayMetrics? = null

    var movieId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate DetailsFragment")
        super.onCreate(savedInstanceState)
        prepareBackgroundManager()
        viewModel = ViewModelProvider(
            this@MovieDetailsFragment,
            MainViewModelFactory(MovieUseCase())
        ).get(MainViewModel::class.java)
        mPresenterSelector = ClassPresenterSelector()
        mAdapter = ArrayObjectAdapter(mPresenterSelector)

        val intent = activity?.intent
         movieId = intent?.getIntExtra("movie_id", 0)!!




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (movieId != null) {
            viewModel.getMovie(movieId)?.observe(viewLifecycleOwner, {
                if (it != null) {
                    mSelectedMovie = it
                    it.backdrop_path?.let { it1 -> updateBackground(it1.replace("w500","original")) }
                    setupDetailsOverviewRowPresenter()
                    setupDetailsOverviewRow()
                    adapter = mAdapter
                }

            })
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedMovie?.toString())
        val row = DetailsOverviewRow(mSelectedMovie)

        val options = RequestOptions()
            .error(R.drawable.default_background)
            .dontAnimate()

        Glide.with(this)
            .asBitmap()
            .load(mSelectedMovie?.poster_path)
            .apply(options)
            .into(object : SimpleTarget<Bitmap?>() {
                override  fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    row.setImageBitmap(activity, resource)
                    startEntranceTransition()
                }

            })

        val actionAdapter = ArrayObjectAdapter()

        actionAdapter.add(
            Action(
                ACTION_WATCH_TRAILER,
                resources.getString(R.string.watch_trailer_1),
                resources.getString(R.string.watch_trailer_2)
            )
        )

        row.actionsAdapter = actionAdapter

        mAdapter.add(row)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager?.attach(requireActivity().window)
        mDefaultBackground = resources.getDrawable(R.drawable.default_background, null)
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun updateBackground(uri: String) {



        Glide.with(this)
            .asBitmap()
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into(object : SimpleTarget<Bitmap?>(mMetrics!!.widthPixels, mMetrics!!.heightPixels) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    mBackgroundManager?.setBitmap(resource)
                }


            })
    }


    private fun setupDetailsOverviewRowPresenter() {
        // Set detail background.
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        detailsPresenter.backgroundColor =
            ContextCompat.getColor(requireActivity(), R.color.selected_background)
        detailsPresenter.initialState = FullWidthDetailsOverviewRowPresenter.STATE_HALF
        // Hook up transition element.
        val sharedElementHelper = FullWidthDetailsOverviewSharedElementHelper()
        sharedElementHelper.setSharedElementEnterTransition(
            activity, "hero"
        )
        detailsPresenter.setListener(sharedElementHelper)
        detailsPresenter.isParticipatingEntranceTransition = false
        prepareEntranceTransition()
        detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
            if (action.id == ACTION_WATCH_TRAILER) {
                val intent = Intent(activity, PlayerActivity::class.java)
                intent.putExtra("movie_id", movieId)
                startActivity(intent)
            } else {
                Toast.makeText(activity, action.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        mPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
        mAdapter = ArrayObjectAdapter(mPresenterSelector)
        adapter = mAdapter
    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val density = context.applicationContext.resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }
        companion object {
            private val TAG = "VideoDetailsFragment"

            private val ACTION_WATCH_TRAILER = 1L
            private val ACTION_RENT = 2L
            private val ACTION_BUY = 3L

            private val DETAIL_THUMB_WIDTH = 274
            private val DETAIL_THUMB_HEIGHT = 274

            private val NUM_COLS = 10
        }
}
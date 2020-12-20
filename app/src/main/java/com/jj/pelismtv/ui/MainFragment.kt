package com.jj.pelismtv.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.jj.pelismtv.R
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.presenter.CardPresenter
import com.jj.pelismtv.presenter.IconHeaderItemPresenter
import com.jj.pelismtv.ui.search.SearchActivity
import com.jj.pelismtv.utils.VideoContract


/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val mHandler = Handler()
    private var mCategoryRowAdapter: ArrayObjectAdapter? = null
    private var mDefaultBackground: Drawable? = null
    private var mMetrics: DisplayMetrics? = null
    private var mBackgroundTask: Runnable? = null
    private val mBackgroundURI: Uri? = null
    private var mBackgroundManager: BackgroundManager? = null
    private var mLoaderManager: LoaderManager? = null

    private var mVideoCursorAdapters: Map<Int, CursorObjectAdapter>? = null

    companion object {
        private val TAG = "MainFragment"
        private val BACKGROUND_UPDATE_DELAY = 300
        private val CATEGORY_LOADER = 200

        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
    }
    private lateinit var viewModel: MainViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Create a list to contain all the CursorObjectAdapters.
        // Each adapter is used to render a specific row of videos in the MainFragment.
        mVideoCursorAdapters = HashMap()

        // Start loading the categories from the database.
        mLoaderManager = LoaderManager.getInstance(this)
        mLoaderManager?.initLoader(CATEGORY_LOADER, null, this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // Final initialization, modifying UI elements.
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this@MainFragment, MainViewModelFactory(MovieUseCase() )).get( MainViewModel::class.java)
        //  fManager = this.parentFragmentManager
        // Prepare the manager that maintains the same background image between activities.
        prepareBackgroundManager()
        setupUIElements()
        setupEventListeners()
        prepareEntranceTransition()

        // Map category results from the database to ListRow objects.
        // This Adapter is used to render the MainFragment sidebar labels.
       mCategoryRowAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = mCategoryRowAdapter

    }

    override fun onDestroy() {
        mHandler.removeCallbacks(mBackgroundTask!!)
        mBackgroundManager = null
        super.onDestroy()
    }

    override fun onStop() {
    //    mBackgroundManager!!.release()
        super.onStop()
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        onItemViewClickedListener = ItemViewClickedListener(this.requireContext())
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager?.attach(requireActivity().window)
        mDefaultBackground = resources.getDrawable(R.drawable.default_background, null)
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupUIElements() {
        badgeDrawable = requireActivity().resources.getDrawable(R.drawable.videos_by_google_banner, null)
        title = getString(R.string.browse_title) // Badge, when set, takes precedent over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireActivity(), R.color.fastlane_background)

        // Set search icon color.
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_opaque)
        setHeaderPresenterSelector(object : PresenterSelector() {
            override fun getPresenter(o: Any): Presenter {
                return IconHeaderItemPresenter()
            }
        })

      //  setHeadersState(HEADERS_DISABLED);

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return if (id == CATEGORY_LOADER) {
            CursorLoader(
                    requireContext(),
                    VideoContract.VideoEntry.CONTENT_URI, arrayOf("DISTINCT " + VideoContract.VideoEntry.COLUMN_CATEGORY),  // Only categories
                    null,  // No selection clause
                    null,  // No selection arguments
                    null // Default sort order
            )
        } else {
            // Assume it is for a video.
            val category = args!!.getString(VideoContract.VideoEntry.COLUMN_CATEGORY)

            // This just creates a CursorLoader that gets all videos.
            CursorLoader(
                    requireContext(),
                    VideoContract.VideoEntry.CONTENT_URI,  // Table to query
                    null, VideoContract.VideoEntry.COLUMN_CATEGORY.toString() + " = ?", arrayOf(category),  // Select based on the category id.
                    null // Default sort order
            )
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

        val header = HeaderItem(0, "Peliculas")

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

        viewModel.getMovies()?.observe(viewLifecycleOwner, {
            if(it != null){

                for ( movie in it){
                    listRowAdapter.add(movie)
                }

                rowsAdapter.add(ListRow(header, listRowAdapter))
                adapter = rowsAdapter
            }

        })

    }


    override fun onLoaderReset(loader: Loader<Cursor>) {
        val loaderId = loader.id
        if (loaderId != CATEGORY_LOADER) {
            mVideoCursorAdapters!![loaderId]!!.changeCursor(null)
        } else {
            mCategoryRowAdapter!!.clear()
        }
    }

     class ItemViewClickedListener(val context: Context) : OnItemViewClickedListener {

        override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any,
                                  rowViewHolder: RowPresenter.ViewHolder, row: Row) {

            if (item is Movie) {
             /*   val video: Movie = item
                val intent = Intent(context, VideoDetailsActivity::class.java)
                intent.putExtra(VideoDetailsActivity.VIDEO, video)
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        (itemViewHolder.view as ImageCardView).mainImageView,
                        VideoDetailsActivity.SHARED_ELEMENT_NAME).toBundle()
                context.startActivity(intent, bundle)*/
            }
        }
    }
}
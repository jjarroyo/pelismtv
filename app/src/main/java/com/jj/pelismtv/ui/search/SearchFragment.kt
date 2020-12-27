package com.jj.pelismtv.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.BuildConfig
import com.jj.pelismtv.R
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.presenter.CardPresenter
import com.jj.pelismtv.ui.MainViewModel
import com.jj.pelismtv.ui.MainViewModelFactory
import com.jj.pelismtv.ui.detail.MovieDetailsActivity
import java.lang.reflect.Field


class SearchFragment: SearchSupportFragment(), SearchSupportFragment.SearchResultProvider{

    companion object {
        private const val TAG = "SearchFragment"
        private val DEBUG: Boolean = BuildConfig.DEBUG
        private const val FINISH_ON_RECOGNIZER_CANCELED = true
        private const val REQUEST_SPEECH = 0x00000010
    }
    private lateinit var viewModel: MainViewModel
    private val mHandler = Handler()
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private var mQuery: String? = null
    private val mVideoCursorAdapter: CursorObjectAdapter = CursorObjectAdapter(CardPresenter())

    private var mSearchLoaderId = 1
    private var mResultsFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    //    mVideoCursorAdapter.mapper = VideoCursorMapper()
        setSearchResultProvider(this)
        setOnItemViewClickedListener(ItemViewClickedListener(requireActivity()))
        viewModel = ViewModelProvider(this@SearchFragment, MainViewModelFactory(MovieUseCase())).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        //activity?.finishActivity(REQ_CODE_SPEECH_INPUT)
        return root
    }

    override fun onPause() {
        mHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_SPEECH -> when (resultCode) {
                Activity.RESULT_OK -> setSearchQuery(data, true)
                else ->                         // If recognizer is canceled or failed, keep focus on the search orb
                    if (FINISH_ON_RECOGNIZER_CANCELED) {
                        if (!hasResults()) {
                            if (DEBUG) Log.v(
                                    TAG,
                                    "Voice search canceled"
                            )
                            requireView().findViewById<View>(R.id.lb_search_bar_speech_orb)
                                    .requestFocus()
                        }
                    }
            }
        }
    }
    override fun getResultsAdapter(): ObjectAdapter? {
        return mRowsAdapter
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        if (DEBUG) Log.i(
                TAG,
                String.format("Search text changed: %s", newQuery)
        )
        if (newQuery != null) {
           // loadQuery(newQuery)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (DEBUG) Log.i(
                TAG,
                String.format("Search text submitted: %s", query)
        )
        if (query != null) {
            loadQuery(query)
        }
        return true
    }


    fun hasResults(): Boolean {
        return mRowsAdapter!!.size() > 0 && mResultsFound
    }



    private fun loadQuery(query: String) {
        if (!TextUtils.isEmpty(query) && query != "nil") {
            mQuery = query

            viewModel.getSearchMovies(mQuery)?.observe(viewLifecycleOwner, {
                if (it != null) {
                    onLoadResult(it)
                }

            })
        }
    }

    fun focusOnSearch() {
        requireView().findViewById<View>(R.id.lb_search_bar).requestFocus()
    }



     fun onLoadResult(data: List<Movie>) {

         val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        val titleRes: Int
        if (data.count() > 0) {
            mResultsFound = true
            titleRes = R.string.search_results

            for (item in data){
                listRowAdapter.add(item)
            }

        } else {
            mResultsFound = false
            titleRes = R.string.no_search_results
        }

        val header = HeaderItem(getString(titleRes, mQuery))
        mRowsAdapter!!.clear()
        val row = ListRow(header, listRowAdapter)
        mRowsAdapter!!.add(row)
    }
    private class ItemViewClickedListener(context: Activity) : OnItemViewClickedListener {
        val context = context
        override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any?,
                                   rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
            if (item is Movie) {

                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("movie_id", item.id)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context,
                    (itemViewHolder.view as ImageCardView).mainImageView, "hero").toBundle()
                context.startActivity(intent, bundle)
            }
        }
    }

}
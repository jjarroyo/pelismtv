package com.jj.pelismtv.ui.search

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.*
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.jj.pelismtv.BuildConfig
import com.jj.pelismtv.R
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.presenter.CardPresenter
import com.jj.pelismtv.utils.VideoContract

class SearchFragment: SearchSupportFragment(), SearchSupportFragment.SearchResultProvider,
LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val TAG = "SearchFragment"
        private val DEBUG: Boolean = BuildConfig.DEBUG
        private const val FINISH_ON_RECOGNIZER_CANCELED = true
        private const val REQUEST_SPEECH = 0x00000010
    }

    private val mHandler = Handler()
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private var mQuery: String? = null
    private val mVideoCursorAdapter: CursorObjectAdapter = CursorObjectAdapter(CardPresenter())

    private var mSearchLoaderId = 1
    private var mResultsFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
     //   mVideoCursorAdapter.mapper = VideoCursorMapper()
        setSearchResultProvider(this)
        setOnItemViewClickedListener(ItemViewClickedListener())
        if (DEBUG) {
            Log.d(
                TAG,
                "User is initiating a search. Do we have RECORD_AUDIO permission? " +
                        hasPermission(Manifest.permission.RECORD_AUDIO)
            )
        }
        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
            if (DEBUG) {
                Log.d(
                    TAG,
                    "Does not have RECORD_AUDIO, using SpeechRecognitionCallback"
                )
            }
            // SpeechRecognitionCallback is not required and if not provided recognition will be
            // handled using internal speech recognizer, in which case you must have RECORD_AUDIO
            // permission
            setSpeechRecognitionCallback {
                try {
                    startActivityForResult(
                        recognizerIntent,
                        REQUEST_SPEECH
                    )
                } catch (e: ActivityNotFoundException) {
                    Log.e(
                        TAG,
                        "Cannot find activity for speech recognizer",
                        e
                    )
                }
            }
        } else if (DEBUG) {
            Log.d(TAG, "We DO have RECORD_AUDIO")
        }
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
            loadQuery(newQuery)
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

    private fun hasPermission(permission: String): Boolean {
        val context: Context? = activity
        return PackageManager.PERMISSION_GRANTED == context!!.packageManager.checkPermission(
            permission, context.packageName
        )
    }

    private fun loadQuery(query: String) {
        if (!TextUtils.isEmpty(query) && query != "nil") {
            mQuery = query
            loaderManager.initLoader(mSearchLoaderId++, null, this)
        }
    }

    fun focusOnSearch() {
        requireView().findViewById<View>(R.id.lb_search_bar).requestFocus()
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val query = mQuery!!
        return CursorLoader(
            requireActivity(),
            VideoContract.VideoEntry.CONTENT_URI,
            null,  // Return all fields.
            VideoContract.VideoEntry.COLUMN_NAME + " LIKE ? OR " +
                    VideoContract.VideoEntry.COLUMN_DESC + " LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null // Default sort order
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val titleRes: Int
        if (data != null && data.moveToFirst()) {
            mResultsFound = true
            titleRes = R.string.search_results
        } else {
            mResultsFound = false
            titleRes = R.string.no_search_results
        }
        mVideoCursorAdapter.changeCursor(data)
        val header = HeaderItem(getString(titleRes, mQuery))
        mRowsAdapter!!.clear()
        val row = ListRow(header, mVideoCursorAdapter)
        mRowsAdapter!!.add(row)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mVideoCursorAdapter.changeCursor(null)
    }

    private class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder, item: Any,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            if (item is Movie) {
         /*       val video: Movie = item as Movie
                val intent = Intent(getActivity(), VideoDetailsActivity::class.java)
                intent.putExtra(VideoDetailsActivity.VIDEO, video)
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    VideoDetailsActivity.SHARED_ELEMENT_NAME
                ).toBundle()
                getActivity().startActivity(intent, bundle)
            } else {
                Toast.makeText(getActivity(), item as String, Toast.LENGTH_SHORT).show()*/
            }
        }
    }
}
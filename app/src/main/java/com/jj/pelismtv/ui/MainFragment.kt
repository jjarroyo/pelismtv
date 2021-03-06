package com.jj.pelismtv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.R
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Genre
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.presenter.CardPresenter
import com.jj.pelismtv.ui.detail.serie.MovieDetailsActivity
import com.jj.pelismtv.ui.grid.GridGenreActivity
import com.jj.pelismtv.ui.search.SearchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment() {

    private val mVideoCursorAdapter = CursorObjectAdapter(CardPresenter())
    private var cols = 50
    companion object {
        private val TAG = "MainFragment"
        private const val NUM_COLUMNS = 6
    }
    var customTitleView:View? = null
    private lateinit var viewModel: MainViewModel
    //private var mAdapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  mVideoCursorAdapter.mapper = VideoCursorMapper()
    //    adapter = mVideoCursorAdapter


        title = getString(R.string.vertical_grid_title)
   //     isHeadersTransitionOnBackEnabled = true
     //   brandColor = resources.getColor(R.color.fastlane_background)
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        setupFragment()

        Handler().postDelayed({
            loadData()
            startEntranceTransition()
        }, 2000)


    }

    private class Adapter(presenter: CardPresenter?) : ArrayObjectAdapter(presenter) {
        fun callNotifyChanged() {
            super.notifyChanged()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // Final initialization, modifying UI elements.
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this@MainFragment, MainViewModelFactory(MovieUseCase())).get(
                MainViewModel::class.java
        )

        setupEventListeners()
        prepareEntranceTransition()



    }


    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
       // onItemViewClickedListener = ItemViewClickedListener(this.requireContext())
    }

    @SuppressLint("CheckResult")
    private fun loadData() {

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        viewModel.getGenreWithMovie()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                if (it != null) {
                    var id = 100000001
                    viewModel.arrayGenres.clear()
                    rowsAdapter.clear()
                    for (data in it) {
                        Log.e(TAG, "mmmmmm " + data.movies.count())
                        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

                        val  sortMovie = data.movies.sortedByDescending { movie -> movie.id  }
                        for ((cont, movie) in sortMovie.withIndex()) {
                            if (cont <= 25) {
                                listRowAdapter.add(movie)
                            }
                            if (cont == 26) {
                                listRowAdapter.add(
                                        Movie(
                                                id,
                                                "Ver mas..",
                                                "",
                                                Date(),
                                                "https://pelismayo.com/imgs/seeall.png",
                                                null,
                                                null,
                                                ""
                                        )
                                )
                                id++
                            }
                        }
                        if (data.movies.size < 25) {
                            listRowAdapter.add(
                                    Movie(
                                            id,
                                            "Ver mas..",
                                            "",
                                            Date(),
                                            "https://pelismayo.com/imgs/seeall.png",
                                            null,
                                            null,
                                            ""
                                    )
                            )
                            id++
                        }
                        viewModel.arrayGenres.add(data.genre)
                        val header = HeaderItem(data.genre.id.toLong(), data.genre.label)
                        rowsAdapter.add(ListRow(header, listRowAdapter))
                    }


                    adapter = rowsAdapter
                } else {
                    Log.e(TAG, "mmmmmm ")
                }

            }


    }

    private fun setupFragment() {
        //  val gridPresenter = VerticalGridPresenter()
        //     gridPresenter.numberOfColumns = NUM_COLUMNS
        //  setGridPresenter(gridPresenter)
        //   loaderManager.initLoader(ALL_VIDEOS_LOADER, null, this)

        // After 500ms, start the animation to transition the cards into view.
        Handler().postDelayed({ startEntranceTransition() }, 500)
        setOnSearchClickedListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        // onItemViewClickedListener = ItemViewClickedListener(requireContext())
        //  mAdapter = Adapter(CardPresenter())
        //  adapter = mAdapter

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->

        }

        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->

            if (item is Movie) {

                val info:Movie = item

                when (info.id) {
                    100000001 -> {
                        if (viewModel.arrayGenres.size > 0) {
                            Log.e("si ", viewModel.arrayGenres[0].label)
                            showGridGenre(viewModel.arrayGenres[0], itemViewHolder)
                        }

                    }
                    100000002 -> {
                        if (viewModel.arrayGenres.size > 1) {
                            Log.e("si ", viewModel.arrayGenres[1].label)
                            showGridGenre(viewModel.arrayGenres[1], itemViewHolder)
                        }
                    }
                    100000003 -> {
                        if (viewModel.arrayGenres.size > 2) {
                            Log.e("si ", viewModel.arrayGenres[2].label)
                            showGridGenre(viewModel.arrayGenres[2], itemViewHolder)
                        }
                    }
                    100000004 -> {
                        if (viewModel.arrayGenres.size > 3) {
                            Log.e("si ", viewModel.arrayGenres[3].label)
                            showGridGenre(viewModel.arrayGenres[3], itemViewHolder)
                        }
                    }
                    100000005 -> {
                        if (viewModel.arrayGenres.size > 4) {
                            Log.e("si ", viewModel.arrayGenres[4].label)
                            showGridGenre(viewModel.arrayGenres[4], itemViewHolder)
                        }
                    }
                    100000006 -> {
                        if (viewModel.arrayGenres.size > 5) {
                            Log.e("si ", viewModel.arrayGenres[5].label)
                            showGridGenre(viewModel.arrayGenres[5], itemViewHolder)
                        }
                    }
                    100000007 -> {
                        if (viewModel.arrayGenres.size > 6) {
                            Log.e("si ", viewModel.arrayGenres[6].label)
                            showGridGenre(viewModel.arrayGenres[6], itemViewHolder)
                        }
                    }
                    100000008 -> {
                        if (viewModel.arrayGenres.size > 7) {
                            Log.e("si ", viewModel.arrayGenres[7].label)
                            showGridGenre(viewModel.arrayGenres[7], itemViewHolder)
                        }
                    }
                    100000009 -> {
                        if (viewModel.arrayGenres.size > 8) {
                            Log.e("si ", viewModel.arrayGenres[8].label)
                            showGridGenre(viewModel.arrayGenres[8], itemViewHolder)
                        }
                    }
                    100000010 -> {
                        if (viewModel.arrayGenres.size > 9) {
                            Log.e("si ", viewModel.arrayGenres[9].label)
                            showGridGenre(viewModel.arrayGenres[9], itemViewHolder)
                        }
                    }
                    100000011 -> {
                        if (viewModel.arrayGenres.size > 10) {
                            Log.e("si ", viewModel.arrayGenres[10].label)
                            showGridGenre(viewModel.arrayGenres[10], itemViewHolder)
                        }
                    }
                    100000012 -> {
                        if (viewModel.arrayGenres.size > 11) {
                            Log.e("si ", viewModel.arrayGenres[11].label)
                            showGridGenre(viewModel.arrayGenres[11], itemViewHolder)
                        }
                    }
                    100000013 -> {
                        if (viewModel.arrayGenres.size > 12) {
                            Log.e("si ", viewModel.arrayGenres[12].label)
                            showGridGenre(viewModel.arrayGenres[12], itemViewHolder)
                        }
                    }
                    100000014 -> {
                        if (viewModel.arrayGenres.size > 13) {
                            Log.e("si ", viewModel.arrayGenres[13].label)
                            showGridGenre(viewModel.arrayGenres[13], itemViewHolder)
                        }
                    }
                    100000015 -> {
                        if (viewModel.arrayGenres.size > 14) {
                            Log.e("si ", viewModel.arrayGenres[14].label)
                            showGridGenre(viewModel.arrayGenres[14], itemViewHolder)
                        }
                    }
                    100000016 -> {
                        if (viewModel.arrayGenres.size > 15) {
                            Log.e("si ", viewModel.arrayGenres[15].label)
                            showGridGenre(viewModel.arrayGenres[15], itemViewHolder)
                        }
                    }
                    100000017 -> {
                        if (viewModel.arrayGenres.size > 16) {
                            Log.e("si ", viewModel.arrayGenres[16].label)
                            showGridGenre(viewModel.arrayGenres[16], itemViewHolder)
                        }
                    }
                    100000018 -> {
                        if (viewModel.arrayGenres.size > 17) {
                            Log.e("si ", viewModel.arrayGenres[17].label)
                            showGridGenre(viewModel.arrayGenres[17], itemViewHolder)
                        }
                    }
                    100000019 -> {
                        if (viewModel.arrayGenres.size > 18) {
                            Log.e("si ", viewModel.arrayGenres[18].label)
                            showGridGenre(viewModel.arrayGenres[18], itemViewHolder)
                        }
                    }
                    100000020 -> {
                        if (viewModel.arrayGenres.size > 19) {
                            Log.e("si ", viewModel.arrayGenres[19].label)
                            showGridGenre(viewModel.arrayGenres[19], itemViewHolder)
                        }
                    }
                    100000021 -> {
                        if (viewModel.arrayGenres.size > 20) {
                            Log.e("si ", viewModel.arrayGenres[20].label)
                            showGridGenre(viewModel.arrayGenres[20], itemViewHolder)
                        }
                    }
                    100000022 -> {
                        if (viewModel.arrayGenres.size > 21) {
                            Log.e("si ", viewModel.arrayGenres[21].label)
                            showGridGenre(viewModel.arrayGenres[21], itemViewHolder)
                        }
                    }
                    100000023 -> {
                        if (viewModel.arrayGenres.size > 22) {
                            Log.e("si ", viewModel.arrayGenres[22].label)
                            showGridGenre(viewModel.arrayGenres[22], itemViewHolder)
                        }
                    }
                    100000024 -> {
                        if (viewModel.arrayGenres.size > 23) {
                            Log.e("si ", viewModel.arrayGenres[23].label)
                            showGridGenre(viewModel.arrayGenres[23], itemViewHolder)
                        }
                    }
                    100000025 -> {
                        if (viewModel.arrayGenres.size > 24) {
                            Log.e("si ", viewModel.arrayGenres[24].label)
                            showGridGenre(viewModel.arrayGenres[24], itemViewHolder)
                        }
                    }
                    100000026 -> {
                        if (viewModel.arrayGenres.size > 25) {
                            Log.e("si ", viewModel.arrayGenres[25].label)
                            showGridGenre(viewModel.arrayGenres[25], itemViewHolder)
                        }
                    }
                    100000027 -> {
                        if (viewModel.arrayGenres.size > 26) {
                            Log.e("si ", viewModel.arrayGenres[26].label)
                            showGridGenre(viewModel.arrayGenres[26], itemViewHolder)
                        }
                    }
                    100000028 -> {
                        if (viewModel.arrayGenres.size > 27) {
                            Log.e("si ", viewModel.arrayGenres[27].label)
                            showGridGenre(viewModel.arrayGenres[27], itemViewHolder)
                        }
                    }
                    100000029 -> {
                        if (viewModel.arrayGenres.size > 28) {
                            Log.e("si ", viewModel.arrayGenres[28].label)
                            showGridGenre(viewModel.arrayGenres[28], itemViewHolder)
                        }
                    }
                    100000030 -> {
                        if (viewModel.arrayGenres.size > 29) {
                            Log.e("si ", viewModel.arrayGenres[29].label)
                            showGridGenre(viewModel.arrayGenres[29], itemViewHolder)
                        }
                    }

                    else -> {

                        val intent = Intent(activity, MovieDetailsActivity::class.java)
                        intent.putExtra("movie_id", item.id)

                        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                requireActivity(),
                                (itemViewHolder.view as ImageCardView).mainImageView, "hero"
                        ).toBundle()
                        requireActivity().startActivity(intent, bundle)
                    }
                }

            }
        }

    }

    fun showGridGenre(genre: Genre, itemViewHolder: Presenter.ViewHolder){
        val intent = Intent(activity, GridGenreActivity::class.java)
        intent.putExtra("genre_id", genre.id)
        intent.putExtra("genre_title", genre.label)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                (itemViewHolder.view as ImageCardView).mainImageView, "hero"
        ).toBundle()
        requireActivity().startActivity(intent, bundle)
    }


    override fun onResume() {
        super.onResume()
        val browseFrameLayout = view?.findViewById<BrowseFrameLayout>(R.id.grid_frame)

        browseFrameLayout?.setOnFocusSearchListener { focused, direction ->
            val button1 = customTitleView!!.findViewById<Button>(R.id.serie_btn)
            if ((focused !== customTitleView && direction == View.FOCUS_UP) ||
                    ( (direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT))) {
                return@setOnFocusSearchListener button1
            }

            if ((button1.hasFocus() || button1.hasFocus()) && direction == View.FOCUS_DOWN) {
                return@setOnFocusSearchListener browseFrameLayout
            }
            browseFrameLayout
        }
    }
}
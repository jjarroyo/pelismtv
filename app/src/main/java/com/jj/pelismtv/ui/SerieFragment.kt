package com.jj.pelismtv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.R
import com.jj.pelismtv.domain.SerieUseCase
import com.jj.pelismtv.model.GenreSerie
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.model.Serie
import com.jj.pelismtv.presenter.SeriePresenter
import com.jj.pelismtv.ui.detail.serie.GridSeasonActivity
import com.jj.pelismtv.ui.detail.serie.MovieDetailsActivity
import com.jj.pelismtv.ui.grid.GridGenreActivity
import com.jj.pelismtv.ui.search.SearchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class SerieFragment : BrowseSupportFragment(){

    private lateinit var viewModel: SerieViewModel
    //private var mAdapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  mVideoCursorAdapter.mapper = VideoCursorMapper()
        //    adapter = mVideoCursorAdapter


        title = getString(R.string.serie_grid_title)
        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        setupFragment()

        Handler().postDelayed({
            loadDataSerie()
            startEntranceTransition()
        }, 2000)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // Final initialization, modifying UI elements.
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this@SerieFragment, SerieViewModelFactory(SerieUseCase())).get(
            SerieViewModel::class.java
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
    private fun loadDataSerie() {

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = SeriePresenter()

        viewModel.getGenreWithSerie()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                if (it != null) {
                    var id = 200000001
                    viewModel.arrayGenres.clear()
                    rowsAdapter.clear()
                    for (data in it) {
                        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

                        val  sortSerie = data.series.sortedByDescending { serie -> serie.id  }
                        for ((cont, serie) in sortSerie.withIndex()) {
                            if (cont <= 25) {
                                listRowAdapter.add(serie)
                            }
                            if (cont == 26) {
                                listRowAdapter.add(
                                    Serie(
                                        id,
                                        "Ver mas..",
                                        "",
                                        Date(),
                                        "https://pelismayo.com/imgs/seeall.png",
                                        null,
                                        0,
                                        ""
                                    )
                                )
                                id++
                            }
                        }
                        if (data.series.size < 25) {
                            listRowAdapter.add(
                                Serie(
                                    id,
                                    "Ver mas..",
                                    "",
                                    Date(),
                                    "https://pelismayo.com/imgs/seeall.png",
                                    null,
                                    0,
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

            if (item is Serie) {

                val info: Serie = item

                when (info.id) {
                    100000001 -> {
                        if (viewModel.arrayGenres.size > 0) {
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

                    else -> {

                        val intent = Intent(activity, GridSeasonActivity::class.java)
                        intent.putExtra("serie_id",  item.id)
                        intent.putExtra("serie_title", item.title)
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

    private fun showGridGenre(genre: GenreSerie, itemViewHolder: Presenter.ViewHolder){
        val intent = Intent(activity, GridGenreActivity::class.java)
        intent.putExtra("genre_id", genre.id)
        intent.putExtra("genre_title", genre.label)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            (itemViewHolder.view as ImageCardView).mainImageView, "hero"
        ).toBundle()
        requireActivity().startActivity(intent, bundle)
    }


}
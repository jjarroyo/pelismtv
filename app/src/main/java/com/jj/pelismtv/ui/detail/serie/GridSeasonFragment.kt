package com.jj.pelismtv.ui.detail.serie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.domain.SerieUseCase
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.model.Season
import com.jj.pelismtv.presenter.CardPresenter
import com.jj.pelismtv.presenter.SeasonPresenter
import com.jj.pelismtv.ui.SerieViewModel
import com.jj.pelismtv.ui.SerieViewModelFactory
import com.jj.pelismtv.ui.search.SearchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GridSeasonFragment: VerticalGridSupportFragment() {
    private val mVideoCursorAdapter = CursorObjectAdapter(CardPresenter())
    private var cols = 50
    companion object {
        private val TAG = "MainFragment"
        private const val NUM_COLUMNS = 6
    }

    var title_serie = ""
    var serieId = 0

    private lateinit var viewModel: SerieViewModel
    private var mAdapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ///  mVideoCursorAdapter.mapper = VideoCursorMapper()
        adapter = mVideoCursorAdapter

        val intent = activity?.intent
        serieId = intent?.getIntExtra("serie_id", 0)!!
        title_serie = intent.getStringExtra("serie_title").toString()
        title = title_serie

        if (savedInstanceState == null) {
            prepareEntranceTransition()
        }
        setupFragment()

        Handler().postDelayed({
            loadData()
            startEntranceTransition()
        }, 2000)


    }

    private class Adapter(presenter: SeasonPresenter?) : ArrayObjectAdapter(presenter) {
        fun callNotifyChanged() {
            super.notifyChanged()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // Final initialization, modifying UI elements.
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this@GridSeasonFragment, SerieViewModelFactory(SerieUseCase())).get(
            SerieViewModel::class.java)

        setupEventListeners()
        prepareEntranceTransition()



    }


    private fun setupEventListeners() {
        setOnSearchClickedListener(null)
        //  onItemViewClickedListener = ItemViewClickedListener(this.requireContext())
    }

    @SuppressLint("CheckResult")
    private fun loadData() {

        //  val header = HeaderItem(0, "Peliculas")

        // val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        //  val cardPresenter = CardPresenter()
        //   val listRowAdapter = ArrayObjectAdapter(cardPresenter)

        viewModel.getSeasons(serieId)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
            if (it != null) {
                mAdapter?.clear()
                for (movie in it) {
                    //  listRowAdapter.add(movie)
                    mAdapter?.add(movie)
                }

                //  Log.e(TAG, "llega $cols")
                //  Log.e(TAG, "llega ${it.count()}")

                mAdapter!!.callNotifyChanged()
            }

        }




    }

    private fun setupFragment() {
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
        //   loaderManager.initLoader(ALL_VIDEOS_LOADER, null, this)

        // After 500ms, start the animation to transition the cards into view.
        Handler().postDelayed({ startEntranceTransition() }, 500)
        setOnSearchClickedListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        // onItemViewClickedListener = ItemViewClickedListener(requireContext())
        mAdapter = Adapter(SeasonPresenter())
        adapter = mAdapter

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->

        }

        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->

            if (item is Season) {
                val intent = Intent(activity, EpisodeActivity::class.java)
                intent.putExtra("serie_id", serieId)
                intent.putExtra("season_id", item.id)
                intent.putExtra("serie_title", title_serie+" - "+item.name)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    (itemViewHolder.view as ImageCardView).mainImageView, "hero").toBundle()
                requireActivity().startActivity(intent, bundle)

            }
        }

    }



}
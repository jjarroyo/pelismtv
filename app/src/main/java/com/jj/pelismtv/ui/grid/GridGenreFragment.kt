package com.jj.pelismtv.ui.grid

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.presenter.CardPresenter
import com.jj.pelismtv.ui.MainViewModel
import com.jj.pelismtv.ui.MainViewModelFactory
import com.jj.pelismtv.ui.detail.serie.MovieDetailsActivity
import com.jj.pelismtv.ui.search.SearchActivity
import java.util.*

class GridGenreFragment:VerticalGridSupportFragment() {


    private val mVideoCursorAdapter = CursorObjectAdapter(CardPresenter())
    private var cols = 50
    companion object {
        private val TAG = "MainFragment"
        private const val NUM_COLUMNS = 6
    }

    var title_genre = ""
    var genre = 0

    private lateinit var viewModel: MainViewModel
    private var mAdapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ///  mVideoCursorAdapter.mapper = VideoCursorMapper()
            adapter = mVideoCursorAdapter

        val intent = activity?.intent
        genre = intent?.getIntExtra("genre_id", 0)!!
        title_genre = intent.getStringExtra("genre_title").toString()
        title = "Peliculas de $title_genre"

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

        viewModel = ViewModelProvider(this@GridGenreFragment, MainViewModelFactory(MovieUseCase())).get(
            MainViewModel::class.java)

        setupEventListeners()
        prepareEntranceTransition()



    }


    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        //  onItemViewClickedListener = ItemViewClickedListener(this.requireContext())
    }

    private fun loadData() {

        //  val header = HeaderItem(0, "Peliculas")

        // val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        //  val cardPresenter = CardPresenter()
        //   val listRowAdapter = ArrayObjectAdapter(cardPresenter)

        viewModel.getMoviesForGenre(cols,genre)?.observe(viewLifecycleOwner, {
             if (it != null) {
                 mAdapter?.clear()
                 for (movie in it) {
                     //  listRowAdapter.add(movie)
                     mAdapter?.add(movie)
                     Log.e(TAG, "llega ${movie.title}")
                 }

               //  Log.e(TAG, "llega $cols")
               //  Log.e(TAG, "llega ${it.count()}")

                 mAdapter!!.callNotifyChanged()
             } else {
                 Log.e(TAG, "mmmmmm ")
             }

         })




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
          mAdapter = Adapter(CardPresenter())
         adapter = mAdapter

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->

            val selectedIndex: Int = mAdapter!!.indexOf(item)

            if (selectedIndex != -1 && (mAdapter!!.size() - 1) == selectedIndex
                    || selectedIndex != -1 && (mAdapter!!.size() - 2) == selectedIndex
                    || selectedIndex != -1 && (mAdapter!!.size() - 3) == selectedIndex
                    || selectedIndex != -1 && (mAdapter!!.size() - 4) == selectedIndex
                    || selectedIndex != -1 && (mAdapter!!.size() - 5) == selectedIndex
                    || selectedIndex != -1 && (mAdapter!!.size() - 6) == selectedIndex ) {
                cols += 50
                viewModel.getMoviesForGenre(cols,genre)?.observe(viewLifecycleOwner, {
                    if (it != null) {
                        mAdapter?.clear()
                        for (movie in it) {
                            //  listRowAdapter.add(movie)
                            mAdapter?.add(movie)
                            Log.e(TAG, "llega ${movie.title}")
                        }


                       // Log.e(TAG, "llega ${it.count()}")

                        mAdapter!!.callNotifyChanged()
                    } else {
                        Log.e(TAG, "mmmmmm ")
                    }

                })



            }
            Log.i(TAG, "row $row")
            // Log.i(TAG, "onItemSelected: ${rowViewHolder.row}")
            //  Log.i(TAG, "onItemSelected: ${rowViewHolder.rowObject}")
        }

        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->

            if (item is Movie) {
                Log.e(TAG, "col ${item.title}")
                    val intent = Intent(activity, MovieDetailsActivity::class.java)
                    intent.putExtra("movie_id", item.id)

                    val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        (itemViewHolder.view as ImageCardView).mainImageView, "hero").toBundle()
                    requireActivity().startActivity(intent, bundle)

            }
        }

    }



}
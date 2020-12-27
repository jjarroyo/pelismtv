package com.jj.pelismtv.ui

import android.util.Log
import androidx.lifecycle.*
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Genre
import com.jj.pelismtv.model.GenreWithMovies
import com.jj.pelismtv.model.Movie
import com.jj.pelismtv.model.Player
import com.jj.pelismtv.vo.Resource
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers

class MainViewModel(val movieUseCase: MovieUseCase) : ViewModel() {
    var arrayPlayer:ArrayList<Player> = ArrayList()
    var arrayGenres:ArrayList<Genre> = ArrayList()

    private var movieLiveData: LiveData<List<Movie>>? = null
    fun getMovies(col: Int): LiveData<List<Movie>>? {
       // if (movieLiveData == null) {
            movieLiveData = movieUseCase.getList(col)
      //  }

        Log.e("buscando", "llega de buscar" )
        return movieLiveData
    }

    private var movieForGenreLiveData: LiveData<List<Movie>>? = null
    fun getMoviesForGenre(col: Int,genre: Int): LiveData<List<Movie>>? {
        // if (movieLiveData == null) {
        movieForGenreLiveData = movieUseCase.getListForGenre(col,genre)
        //  }

        Log.e("buscando", "llega de buscar" )
        return movieForGenreLiveData
    }



    private var movieSearchLiveData: LiveData<List<Movie>>? = null
    fun getSearchMovies(search: String?): LiveData<List<Movie>>? {
         if (search != null) {
             Log.e("si entra3",search)
             movieSearchLiveData = movieUseCase.getSearchMovie(search)
          }

        Log.e("buscando", "llega de buscar2" )
        return movieSearchLiveData
    }

    private var movieSelectLiveData: LiveData<Movie>? = null
    fun getMovie(id: Int): LiveData<Movie>? {
            movieSelectLiveData = movieUseCase.getMovie(id)
    //    }

        return movieSelectLiveData
    }

    private var genreWithMovieSLiveData: Flowable<List<GenreWithMovies>>? = null
    fun getGenreWithMovie(): Flowable<List<GenreWithMovies>>? {
        genreWithMovieSLiveData = movieUseCase.getGenresWithMovies()
        //    }

        return genreWithMovieSLiveData
    }


    private val movie = MutableLiveData<Int>()

    fun setMovie(id:Int){
        movie.value = id
    }

    val videoMovie = movie.distinctUntilChanged().switchMap {
        liveData(Dispatchers.IO) {
            val data = movieUseCase.getVideoInfo(it) // getList is a suspend function.
            emit(Resource.Loading())
            try {
                emit(data)
            } catch (ex: Exception) {
                //emit(Resource.Failure(ex))
            }
        }

    }

}
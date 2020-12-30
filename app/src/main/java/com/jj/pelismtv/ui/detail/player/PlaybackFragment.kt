package com.jj.pelismtv.ui.detail.player

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.htetznaing.lowcostvideo.LowCostVideo
import com.htetznaing.lowcostvideo.Model.XModel
import com.jj.pelismtv.R
import com.jj.pelismtv.domain.MovieUseCase
import com.jj.pelismtv.model.Quality
import com.jj.pelismtv.ui.MainViewModel
import com.jj.pelismtv.ui.MainViewModelFactory
import com.jj.pelismtv.utils.ThreadManager
import com.jj.pelismtv.utils.servers.Clipwatching
import com.jj.pelismtv.utils.servers.Uqload
import com.jj.pelismtv.vo.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


class PlaybackFragment: VideoSupportFragment() {

    companion object {
        private const val UPDATE_DELAY = 16
    }
    private var settings: SharedPreferences? = null
    private var currentMinuteMovie = 0L

    private lateinit var viewModel: MainViewModel
    private var mPlayerGlue: VideoPlayerGlue? = null
    private var mPlayerAdapter: LeanbackPlayerAdapter? = null
    private var mPlayer: SimpleExoPlayer? = null
    private var mTrackSelector: TrackSelector? = null
    private var movieId = 0

    //---------------------------------
    private var currentQuality = 0
    private var qualitys:ArrayList<Quality> = ArrayList()
    private var currentPlayer = 0
    var headers: HashMap<String, String> = HashMap()
    private var cookie:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = activity?.intent
        movieId = intent?.getIntExtra("movie_id", 0)!!
        viewModel = ViewModelProvider(
                this@PlaybackFragment,
                MainViewModelFactory(MovieUseCase())
        ).get(MainViewModel::class.java)

        settings = PreferenceManager.getDefaultSharedPreferences(requireContext())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovie(movieId)?.observe(viewLifecycleOwner, {
            if (it != null) {
                mPlayerGlue!!.title = it.title
            }

        })
        viewModel.setMovie(movieId)
       // GlobalScope.launch(Dispatchers.Main) {
            viewModel.videoMovie.observe(viewLifecycleOwner, Observer {

                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val data = it.data
                        if (data.count() > 0) {
                            for (item in data) {
                                Log.e("video", item.link)
                                viewModel.arrayPlayer.add(item)
                            }

                            val url = data[0].link
                            val url2 = settings?.getString("movie_" + movieId + "_link", "") ?: ""
                            currentMinuteMovie = settings?.getLong("movie_$movieId", 0L) ?: 0L
                            Log.e("urlg", url2)
                            val minute = "" + millisecToTime(currentMinuteMovie)
                            Log.e("minutr", minute)
                            if (url2.isNotEmpty()) {
                                Log.e("mmm", "ke paso1")
                                AlertDialog.Builder(requireActivity(), R.style.Theme_AppCompat)
                                        .setTitle("Notificacion")
                                        .setMessage(
                                                "desea continuar la pelicula por el minuto ${
                                                    minute.replace(
                                                            ",",
                                                            ":"
                                                    ).replace(".", ":")
                                                }"
                                        )
                                        .setNegativeButton("Cancelar") { dialog, which ->
                                            dialog.dismiss()
                                            validateUrl(url, false)
                                            currentMinuteMovie = 0L
                                        }
                                        .setPositiveButton("Continuar") { dialog, which ->
                                            dialog.dismiss()
                                            validateUrl(url2, true)

                                        }
                                        .show()
                                Log.e("mmm", "ke paso")
                            }else {
                                validateUrl(url, false)
                            }

                           // validateUrl(url)
                        } else {
                            Log.e("url", "no encontro na")
                        }
                    }
                    is Resource.Failure -> {
                        Log.e("url", "fail")
                    }
                }

            })

    }

    fun millisecToTime(millisec: Long): String {
        val sec = millisec / 1000
        val second = sec % 60
        var minute = sec / 60
        if (minute >= 60) {
            val hour = minute / 60
            minute %= 60
            return hour.toString() + ":" + (if (minute < 10) "0$minute" else minute) + ":" + if (second < 10) "0$second" else second
        }
        return minute.toString() + ":" + if (second < 10) "0$second" else second
    }

    override fun onVideoSizeChanged(width: Int, height: Int) {
        super.onVideoSizeChanged(width, height)
        val rootView = view
        val surfaceView = surfaceView
        val params = surfaceView.layoutParams
        params.height = rootView!!.height
        params.width = rootView.width
        surfaceView.layoutParams = params
    }


    private fun validateUrl(url: String, chek: Boolean? = false) {

        when {
            url.contains("uqload") -> {
                Log.e("uqload", "url$url")
                GlobalScope.launch {
                    val uqload = Uqload()
                    qualitys = uqload.getLink(url)
                    headers["Referer"] = url

                    chekUrl()
                }

            }
            url.contains("zplay") -> {
                Log.e("zplayer", "url$url")
                GlobalScope.launch {
                    val clipwatching = Clipwatching()
                    qualitys = clipwatching.getLink(url)
                    headers["Referer"] = url
                    chekUrl()
                }
            }
            else -> {
                Log.e("gdrive", "url$url")
                ThreadManager.executeInMainThread {
                    var new_url = url
                    if (url.contains("/file/d/")) {
                        //  new_url = url.replace("file/d/", "open?id=")
                        new_url = new_url.replace("/preview", "/view?usp=sharing")

                    } else if (url.contains("uc?")) {
                        new_url = url.replace("uc?", "open?")

                    }
                    Log.e("gdrive2", "url$new_url")
                    val xGetter = LowCostVideo(requireActivity())

                    qualitys = ArrayList()
                    xGetter.onFinish(object : LowCostVideo.OnTaskCompleted {
                        override fun onTaskCompleted(
                                vidURL: java.util.ArrayList<XModel>?,
                                multiple_quality: Boolean
                        ) {
                            Log.e("llega aqui o ke", "dos")
                            if (multiple_quality) {
                                if (vidURL != null) {
                                    //This video you can choose qualities
                                    for (model in vidURL) {
                                        qualitys.add(
                                                Quality(
                                                        model.quality,
                                                        model.url,
                                                        "mp4",
                                                        model.cookie
                                                )
                                        )
                                    }
                                }
                            } else {
                                if (vidURL != null) {
                                    qualitys.add(
                                            Quality(
                                                    vidURL[0].quality,
                                                    vidURL[0].url,
                                                    "mp4",
                                                    vidURL[0].cookie
                                            )
                                    )

                                }
                            }
                            Log.e("llega aqui o ke", "mmm")
                            chekUrl()
                        }

                        override fun onError() {
                            chekUrl()
                        }

                    })

                    xGetter.find(new_url)
                }
            }
        }
    }


    private fun chekUrl(){

        if(qualitys.size > 0){
            play()
        }else{
            if (viewModel.arrayPlayer.size > (currentPlayer + 1)) {
                currentPlayer++
                Log.e("siguiente", "" + viewModel.arrayPlayer[currentPlayer].link)
                validateUrl(viewModel.arrayPlayer[currentPlayer].link)
            } else {
                ThreadManager.executeInMainThread {
                  //  videoPlayerLoading.visibility = View.GONE
                   // errorLayout.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer()
        }
    }

    /** Pauses the player.  */
    @TargetApi(Build.VERSION_CODES.N)
    override fun onPause() {
        super.onPause()
        if (mPlayerGlue != null && mPlayerGlue?.isPlaying == true) {
            mPlayerGlue?.pause()
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()

        if(qualitys.size > 0){
            val t = mPlayerAdapter?.currentPosition?:mPlayerGlue?.currentPosition?:mPlayer?.currentPosition
            Log.e("position 1",""+mPlayerAdapter?.currentPosition)
            Log.e("position 2",""+mPlayerGlue?.currentPosition)
            Log.e("position 3",""+mPlayer?.currentPosition)
            val link = qualitys[currentQuality].qualityUrl
            val referer = viewModel.arrayPlayer[currentPlayer].link
            val editor = settings?.edit()
            editor?.putLong("movie_$movieId", t!!)
            editor?.putString("movie_" + movieId + "_link", referer)
            editor?.putString("movie_" + movieId + "_cookie", qualitys[currentQuality].cokies)
            editor?.putString("movie_" + movieId + "_quality", qualitys[currentQuality].quality)
            editor?.putString("movie_" + movieId + "_referer", referer)
            editor?.apply()
        }

        if (Util.SDK_INT > 23) {
            releasePlayer()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (mPlayerGlue != null && mPlayerGlue?.isPlaying == true) {

            if (qualitys.size > 0) {
                val t = mPlayerAdapter?.currentPosition?:mPlayerGlue?.currentPosition
                val link = qualitys[currentQuality].qualityUrl
                val referer = viewModel.arrayPlayer[currentPlayer].link
                val editor = settings?.edit()
                editor?.putLong("movie_$movieId", t!!)
                editor?.putString("movie_" + movieId + "_link", referer)
                editor?.putString("movie_" + movieId + "_cookie", qualitys[currentQuality].cokies)
                editor?.putString("movie_" + movieId + "_quality", qualitys[currentQuality].quality)
                editor?.putString("movie_" + movieId + "_referer", referer)
                editor?.apply()
            }
        }
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {


  /*      mPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        val params = playerView.layoutParams as  ConstraintLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        mPlayer.layoutParams = params
*/
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(bandwidthMeter)
        mTrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                requireActivity(),
                mTrackSelector as DefaultTrackSelector
        )
        mPlayerAdapter = LeanbackPlayerAdapter(
                requireActivity(),
                mPlayer!!,
                UPDATE_DELAY
        )


        mPlayerAdapter?.setProgressUpdatingEnabled(true)
        mPlayer?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        mPlayerGlue = VideoPlayerGlue(requireActivity(), mPlayerAdapter!!)
        val host = VideoSupportFragmentGlueHost(this)
        mPlayerGlue?.host = host
        mPlayerGlue?.playWhenPrepared()

    }

    private fun play() {
        if(qualitys.size > 0){
            var url = qualitys[currentQuality].qualityUrl
            for ((cont, quality) in qualitys.withIndex()){
                if(quality.quality == "720p"){
                    url = quality.qualityUrl
                    currentQuality = cont
                }
            }


            cookie = qualitys[currentQuality].cokies.toString()
            buildMediaSource(Uri.parse(url))
            mPlayerGlue!!.play()
            if(currentMinuteMovie > 0L){
                mPlayerGlue!!.minutePreference(currentMinuteMovie)
            }

        }

    }

    fun rewind() {
        mPlayerGlue!!.rewind()
    }

    fun fastForward() {
        mPlayerGlue!!.fastForward()
    }

    private fun releasePlayer() {
        if (mPlayer != null) {
            mPlayer!!.release()
            mPlayer = null
            mTrackSelector = null
            mPlayerGlue = null
            mPlayerAdapter = null
        }
    }

    private fun prepareMediaForPlaying(mediaSourceUri: Uri) {
        val userAgent = Util.getUserAgent(requireActivity(), "VideoPlayerGlue")
        val mediaSource: MediaSource = ExtractorMediaSource(
                mediaSourceUri,
                DefaultDataSourceFactory(activity, userAgent),
                DefaultExtractorsFactory(),
                null,
                null
        )
        mPlayer!!.prepare(mediaSource)
    }
    private fun buildMediaSource(uri: Uri) {
        val userAgent = "exoplayer-codelab"

        var dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(requireActivity(), userAgent)

        //If google drive you need to set custom cookie
        if (cookie != null) {
            val httpDataSourceFactory = DefaultHttpDataSourceFactory(userAgent, null)
            httpDataSourceFactory.defaultRequestProperties["Cookie"] = cookie
            dataSourceFactory = DefaultDataSourceFactory(
                    requireContext(),
                    null,
                    httpDataSourceFactory
            )
        }

        if(headers.isNotEmpty()){
            val httpDataSourceFactory2 = DefaultHttpDataSourceFactory(userAgent, null)
            httpDataSourceFactory2.defaultRequestProperties["Referer"]  = headers["Referer"]
            dataSourceFactory = DefaultDataSourceFactory(
                    requireContext(),
                    null,
                    httpDataSourceFactory2
            )
        }

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(
                uri
        )

        mPlayer!!.prepare(mediaSource)
    }


}
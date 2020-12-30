package com.jj.pelismtv.ui.detail.player

import android.content.Context
import android.util.Log
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow.*
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import java.util.concurrent.TimeUnit

class VideoPlayerGlue(
        context: Context,
        mPlayerAdapter: LeanbackPlayerAdapter
) :
    PlaybackTransportControlGlue<LeanbackPlayerAdapter>(context, mPlayerAdapter) {
    private val TEN_SECONDS = TimeUnit.SECONDS.toMillis(30)

    private var mFastForwardAction: FastForwardAction? = null
    private var mRewindAction: RewindAction? = null

    init {

        mFastForwardAction = FastForwardAction(context)
        mRewindAction = RewindAction(context)

    }


    override fun onCreateSecondaryActions(adapter: ArrayObjectAdapter) {
        super.onCreateSecondaryActions(adapter)
       // adapter.add(mThumbsDownAction)
       // adapter.add(mThumbsUpAction)
       // adapter.add(mRepeatAction)
    }

    override fun onActionClicked(action: Action) {
        if (shouldDispatchAction(action)) {
            dispatchAction(action)
            return
        }
        // Super class handles play/pause and delegates to abstract methods next()/previous().
        super.onActionClicked(action)
    }

    // Should dispatch actions that the super class does not supply callbacks for.
    private fun shouldDispatchAction(action: Action): Boolean {
        return action === mRewindAction || action === mFastForwardAction
    }

    private fun dispatchAction(action: Action) {
        // Primary actions are handled manually.
        if (action === mRewindAction) {
            rewind()
        } else if (action === mFastForwardAction) {
            fastForward()
        } else if (action is MultiAction) {
            action.nextIndex()
            // Notify adapter of action changes to handle secondary actions, such as, thumbs up/down
            // and repeat.
            notifyActionChanged(
                    action,
                    controlsRow.secondaryActionsAdapter as ArrayObjectAdapter)
        }
    }

    private fun notifyActionChanged(
            action: MultiAction, adapter: ArrayObjectAdapter?) {
        if (adapter != null) {
            val index = adapter.indexOf(action)
            if (index >= 0) {
                adapter.notifyArrayItemRangeChanged(index, 1)
            }
        }
    }

    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        // Order matters, super.onCreatePrimaryActions() will create the play / pause action.
        // Will display as follows:
        // play/pause, previous, rewind, fast forward, next
        //   > /||      |<        <<        >>         >|
        super.onCreatePrimaryActions(adapter)
        adapter.add(mRewindAction)
        adapter.add(mFastForwardAction)
    }

    /** Skips backwards 10 seconds.  */
    fun rewind() {
        Log.e("position current2",""+currentPosition)
        var newPosition: Long = currentPosition - TEN_SECONDS
        newPosition = if (newPosition < 0) 0 else newPosition
        playerAdapter.seekTo(newPosition)
    }

    /** Skips forward 10 seconds.  */
    fun fastForward() {
        if (duration > -1) {
            Log.e("position current",""+currentPosition)
            var newPosition: Long = currentPosition + TEN_SECONDS
            newPosition = if (newPosition > duration) duration else newPosition
            playerAdapter.seekTo(newPosition)
        }
    }

    fun minutePreference(currentMinute:Long){
        Log.e("position current exit",""+currentMinute)
        playerAdapter.seekTo(currentMinute)
    }
}

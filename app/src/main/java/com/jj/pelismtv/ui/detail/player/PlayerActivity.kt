package com.jj.pelismtv.ui.detail.player

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.jj.pelismtv.R
import com.jj.pelismtv.ui.search.LeanbackActivity


class PlayerActivity : LeanbackActivity() {

    companion object {
        private const val GAMEPAD_TRIGGER_INTENSITY_ON = 0.5f
        private const val GAMEPAD_TRIGGER_INTENSITY_OFF = 0.45f
    }

    private var gamepadTriggerPressed = false
    private var mPlaybackFragment: PlaybackFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(1024, 1024)
        window.addFlags(128)
        setContentView(R.layout.fragment_player_activity)

        val fragment = supportFragmentManager.findFragmentByTag(getString(R.string.playback_tag))
        if (fragment is PlaybackFragment) {
            mPlaybackFragment = fragment
        }
    }
    override fun onStop() {
        super.onStop()
        finish()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BUTTON_L2) {
            mPlaybackFragment!!.rewind()
        } else if (keyCode == KeyEvent.KEYCODE_BUTTON_R2) {
            mPlaybackFragment!!.fastForward()
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        // This method will handle gamepad events.
        if (event.getAxisValue(MotionEvent.AXIS_LTRIGGER) > GAMEPAD_TRIGGER_INTENSITY_ON
            && !gamepadTriggerPressed
        ) {
            mPlaybackFragment?.rewind()
            gamepadTriggerPressed = true
        } else if (event.getAxisValue(MotionEvent.AXIS_RTRIGGER) > GAMEPAD_TRIGGER_INTENSITY_ON
            && !gamepadTriggerPressed
        ) {
            mPlaybackFragment?.fastForward()
            gamepadTriggerPressed = true
        } else if (event.getAxisValue(MotionEvent.AXIS_LTRIGGER) < GAMEPAD_TRIGGER_INTENSITY_OFF
            && event.getAxisValue(MotionEvent.AXIS_RTRIGGER) <GAMEPAD_TRIGGER_INTENSITY_OFF
        ) {
            gamepadTriggerPressed = false
        }
        return super.onGenericMotionEvent(event)
    }
}
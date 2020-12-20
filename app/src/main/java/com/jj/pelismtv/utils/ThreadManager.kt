package com.jj.pelismtv.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.Executors.newSingleThreadExecutor

class ThreadManager {

    companion object {

        fun delay(milliseconds: Long, runnable: () -> Unit) {
            try {
                val handler = Handler()
                handler.postDelayed(runnable, milliseconds)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        fun executeInMainThread(runnable: () -> Unit) {
            val handler = Handler(Looper.getMainLooper())
            handler.post(runnable)
        }

        private val backgroundThread = newSingleThreadExecutor()
        fun executeInBackgroundThread(runnable: () -> Unit) {
            backgroundThread.execute { runnable() }
        }

        private val multiBackgroundThread = newFixedThreadPool(8)
        fun executeCallRequest(runnable: () -> Unit) {
            multiBackgroundThread.execute { runnable() }
        }

    }
}
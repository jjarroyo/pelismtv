package com.jj.pelismtv

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.room.Room
import com.jj.pelismtv.data.DataBase
import com.jj.pelismtv.model.MIGRATION_1_2
import com.jj.pelismtv.model.MIGRATION_2_3
import com.jj.pelismtv.model.MIGRATION_3_4


class AppDelegate : Application() {
    companion object {
        var database: DataBase? = null

        fun networkStatus(context:Context): Boolean {

            val connectivityManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val actNetInfo = connectivityManager.activeNetworkInfo

            if (actNetInfo != null && actNetInfo.isConnected) {
                return true
            }
            return false
        }
    }

    private var settings: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, DataBase::class.java, "pelismtv")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).fallbackToDestructiveMigration().build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) initChannels()

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun initChannels() {
   /*     val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createChannel(
            manager,
            StartBackground.CHANNEL_UPDATES_RUNNING,
            StartBackground.CHANNEL_UPDATES_RUNNING_DESC,
            "Descarga de actualizacion",
            NotificationManager.IMPORTANCE_LOW,
            Color.BLUE,
            false
        )
        createChannel(
            manager,
            StartBackground.CHANNEL_UPDATES,
            StartBackground.CHANNEL_UPDATES_DESC,
            "Actualizaciones",
            NotificationManager.IMPORTANCE_MAX,
            Color.BLUE,
            true
        )

*/
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(
        manager: NotificationManager,
        id: String,
        desc: String,
        name: String,
        importance: Int,
        ligths: Int,
        vibration: Boolean
    ) {
        createChannel(manager, id, desc, name, importance, ligths, vibration, false)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(
        manager: NotificationManager,
        id: String,
        desc: String,
        name: String,
        importance: Int,
        ligths: Int,
        vibration: Boolean,
        badge: Boolean
    ) {
        try {
            val channel = NotificationChannel(id, name, importance)
            channel.description = desc
            channel.enableLights(true)
            if (ligths != -1) channel.lightColor = ligths
            if (vibration) {
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 100, 500)
            }
            if (badge) channel.setShowBadge(true)
            manager.createNotificationChannel(channel)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
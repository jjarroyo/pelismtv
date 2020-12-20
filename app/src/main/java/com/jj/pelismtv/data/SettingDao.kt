package com.jj.pelismtv.data

import androidx.room.*
import com.jj.pelismtv.model.Setting
@Dao
interface SettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertSetting(setting: Setting)

    @Update
    suspend  fun updateSetting(setting: Setting)

    @Delete
    suspend  fun deleteSetting(setting: Setting)

    @Query("SELECT * FROM settings")
    suspend fun getSettings(): Array<Setting>

    @Query("SELECT * FROM settings WHERE filter = :filter")
    suspend fun geSetting(filter:String): Setting?

}
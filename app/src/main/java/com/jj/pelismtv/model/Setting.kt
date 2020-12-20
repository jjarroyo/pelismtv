package com.jj.pelismtv.model

import androidx.room.Entity

@Entity(tableName = "settings", primaryKeys = ["filter"])
data class Setting(
    val filter: String,
    val value: String? = null
)
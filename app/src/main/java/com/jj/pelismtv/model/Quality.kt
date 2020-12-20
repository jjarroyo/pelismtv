package com.jj.pelismtv.model

data class Quality(
    val quality: String,
    val qualityUrl: String ,
    val format: String,
    val cokies: String? = null
)
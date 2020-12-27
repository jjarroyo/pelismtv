package com.jj.pelismtv.utils

import android.app.SearchManager
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

 class  VideoContract {


     companion object {

        // The name for the entire content provider.
        @JvmStatic
        val CONTENT_AUTHORITY = "com.jj.pelismtv"

        // Base of all URIs that will be used to contact the content provider.
        @JvmStatic
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

        // The content paths.
        @JvmStatic
        val PATH_VIDEO = "video"

    }

     class VideoEntry : BaseColumns {
         companion object {
         val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build()
         val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "." + PATH_VIDEO

         // Name of the video table.
         val TABLE_NAME = "video"

         // Column with the foreign key into the category table.
         val COLUMN_CATEGORY = "category"

         // Name of the video.
         val COLUMN_NAME = "title"

         // Description of the video.
         val COLUMN_DESC = SearchManager.SUGGEST_COLUMN_TEXT_2

         // The url to the video content.
         val COLUMN_VIDEO_URL = "video_url"

         // The url to the background image.
         val COLUMN_BG_IMAGE_URL = "bg_image_url"

         // The studio name.
         val COLUMN_STUDIO = "studio"

         // The card image for the video.
         val COLUMN_CARD_IMG = SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE

         // The content type of the video.
         val COLUMN_CONTENT_TYPE = SearchManager.SUGGEST_COLUMN_CONTENT_TYPE

         // Whether the video is live or not.
         val COLUMN_IS_LIVE = SearchManager.SUGGEST_COLUMN_IS_LIVE

         // The width of the video.
         val COLUMN_VIDEO_WIDTH = SearchManager.SUGGEST_COLUMN_VIDEO_WIDTH

         // The height of the video.
         val COLUMN_VIDEO_HEIGHT = SearchManager.SUGGEST_COLUMN_VIDEO_HEIGHT

         // The audio channel configuration.
         val COLUMN_AUDIO_CHANNEL_CONFIG = SearchManager.SUGGEST_COLUMN_AUDIO_CHANNEL_CONFIG

         // The purchase price of the video.
         val COLUMN_PURCHASE_PRICE = SearchManager.SUGGEST_COLUMN_PURCHASE_PRICE

         // The rental price of the video.
         val COLUMN_RENTAL_PRICE = SearchManager.SUGGEST_COLUMN_RENTAL_PRICE

         // The rating style of the video.
         val COLUMN_RATING_STYLE = SearchManager.SUGGEST_COLUMN_RATING_STYLE

         // The score of the rating.
         val COLUMN_RATING_SCORE = SearchManager.SUGGEST_COLUMN_RATING_SCORE

         // The year the video was produced.
         val COLUMN_PRODUCTION_YEAR = SearchManager.SUGGEST_COLUMN_PRODUCTION_YEAR

         // The duration of the video.
         val COLUMN_DURATION = SearchManager.SUGGEST_COLUMN_DURATION

         // The action intent for the result.
         val COLUMN_ACTION = SearchManager.SUGGEST_COLUMN_INTENT_ACTION

         // Returns the Uri referencing a video with the specified id.
         fun buildVideoUri(id: Long): Uri {
             return ContentUris.withAppendedId(CONTENT_URI, id)
         }
     }

     }
 }
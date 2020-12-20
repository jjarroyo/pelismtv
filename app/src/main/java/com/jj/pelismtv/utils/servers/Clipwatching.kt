package com.jj.pelismtv.utils.servers

import android.util.Base64
import android.util.Log
import com.jj.pelismtv.model.Quality
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.nio.charset.StandardCharsets

class Clipwatching {

    val link = ArrayList<Quality>()

    fun getLink(url: String): ArrayList<Quality> {

        var document: Document? = null
        var mp4: String? = null
        try {
            document = Jsoup.connect(url)
                .timeout(10000)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .userAgent("Mozilla")
                .parser(Parser.htmlParser()).get()
            try {
                //
                val apiURL: String = "http://18.223.126.66:4000/api/v1/zplayer"
                val obj = Jsoup.connect(apiURL)
                    .timeout(10000)
                    .followRedirects(true)
                    .data("mode", "local")
                    .data("auth", "")
                    .data("source", encodeMSG(document.toString()))
                    .method(Connection.Method.POST)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .execute().body()

                largeLog("response",obj)
                if (obj != null && obj.contains("url")) {
                    val json = JSONObject(obj)
                    if (json.getString("status") == "ok") mp4 = json.getString("url")
                }
            } catch (er: Exception) {
                Log.e("error1", er.localizedMessage)
            }
        } catch (e: Exception) {
            Log.e("error2", e.localizedMessage)
        }

        if(mp4 != null){
            Log.e("mp4", mp4)
            link.add(Quality("480p", mp4, "mp4", null))
        }else{
            Log.e("error", "mp4")
        }
        return link
    }


    fun encodeMSG(msg: String): String? {
        val data = msg.toByteArray(StandardCharsets.UTF_8)
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun largeLog(tag: String?, content: String) {
        if (content.length > 4000) {
            Log.e(tag, content.substring(0, 4000))
            largeLog(tag, content.substring(4000))
        } else {
            Log.e(tag, content)
        }
    }
}
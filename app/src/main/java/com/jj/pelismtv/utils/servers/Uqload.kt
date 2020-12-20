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


class Uqload {

    val link = ArrayList<Quality>()

    fun getLink(url: String): ArrayList<Quality> {
        var l = url

        l = if (l.contains("/embed-")) l else "https://uqload.com/embed-" + l.split("/".toRegex())
            .toTypedArray()[3]
        var document: Document? = null
        var mp4: String? = null
        try {
            Log.e("uqload", l)
            document = Jsoup.connect(l)
                .userAgent("Mozilla")
                .timeout(0)
                .parser(Parser.htmlParser()).get()

            try {
                //
                val apiURL: String = "https://stracktor.herokuapp.com/api/v1/uqload"
                val obj = Jsoup.connect(apiURL)
                    .data("source", encodeMSG(document.toString()))
                    .data("auth", "")
                    .method(Connection.Method.POST)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .execute().body()
                Log.e("obj", obj)
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

}
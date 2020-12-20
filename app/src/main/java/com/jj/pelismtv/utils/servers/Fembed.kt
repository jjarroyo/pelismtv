package com.jj.pelismtv.utils.servers
import android.util.Log
import com.jj.pelismtv.model.Quality
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Fembed {


     fun getQalitys(url: String):ArrayList<Quality>{
        val qualitis = ArrayList<Quality>()
         Log.e("si llega","mmmasas")

        val temp_url = url.replace("/v/", "/api/source/")
        val doc: Document = Jsoup.connect(temp_url).userAgent("Mozilla/5.0 (X11; CrOS x86_64 11895.118.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.159 Safari/537.36")
            .ignoreContentType(true)
            .post()

        val data = doc.body().ownText()
         Log.e("data",data)
        try {

            if(data != null){
                val json = JSONObject(data)
                val array_data: JSONArray = json.getJSONArray("data")
                for (i in 0 until array_data.length()) {
                    val obj = array_data.getJSONObject(i)
                    qualitis.add(Quality(obj.getString("label").toString(),obj.getString("file").toString(),obj.getString("type").toString()))
                }
            }

        }catch (e:Exception){
            Log.e("2", e.message.toString())
        }

        return qualitis
    }


    fun getQalitys2(url: String):ArrayList<Quality>{
        val qualitis = ArrayList<Quality>()
        try {
        val file: String = url.split("/")[4]
        val temp_url = "https://feurl.com/api/source/$file"
        Log.e("temp ",temp_url)
        val doc:String = Jsoup.connect(temp_url)
            .timeout(50000)
            .data("r", "")
            .data("d", "feurl.com")
            .method(Connection.Method.POST)
            .ignoreContentType(true)
            .execute().body()

        Log.e("si llega",doc)
        val data = doc
        Log.e("data",data)


            if(data != null){
                val json = JSONObject(data)
                val array_data: JSONArray = json.getJSONArray("data")
                for (i in 0 until array_data.length()) {
                    val obj = array_data.getJSONObject(i)
                    qualitis.add(Quality(obj.getString("label").toString(),obj.getString("file").toString(),obj.getString("type").toString()))
                }
            }

        }catch (e:Exception){
            Log.e("2", e.message.toString())
        }

        return qualitis
    }

}
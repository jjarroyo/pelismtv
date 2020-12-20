package com.jj.pelismtv.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService


class Common {

    companion object{
        fun formatData(tit: String): String {
            var array = tit
            array = array.replace("[\"", "")
                .replace("\"]", "")
                .replace("\",\"", ":::")
                .replace("\\/", "/")
                .replace("â\u0098\u0086", "\u2606")
                .replace("&#039;", "\'")
                .replace("&iacute;", "í")
                .replace("&deg;", "°")
                .replace("&amp;", "&")
                .replace("&Delta;", "\u0394")
                .replace("&acirc;", "\u00C2")
                .replace("&egrave;", "\u00E8")
                .replace("&middot;", "\u00B7")
                .replace("&#333;", "\u014D")
                .replace("&#9834;", "\u266A")
                .replace("&aacute;", "á")
                .replace("&oacute;", "ó")
                .replace("&quot;", "\"")
                .replace("&uuml;", "\u00FC")
                .replace("&auml;", "\u00E4")
                .replace("&szlig;", "\u00DF")
                .replace("&radic;", "\u221A")
                .replace("&dagger;", "\u2020")
                .replace("&hearts;", "\u2665")
                .replace("&infin;", "\u221E")
                .replace("♪", "\u266A")
                .replace("â\u0099ª", "\u266A")
                .replace("&Psi;", "\u03A8")
                .replace("<p>", "")
                .replace("</p>", "")

            return array
        }

        fun formatitle(tit: String): String {
            var array = tit
            array = array.replace("/", "")
                .replace("\\", "")
                .replace(",", "")
                .replace(".", "")
                .replace("ó", "o")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ú", "u")
                .replace(":", "u")
                .replace("::", "u")
            return array
        }

    }

}
package com.japharr.videoplayer.common.extension

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Japharr on 8/5/2018.
 */
val urlPattern = Pattern.compile(
        "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
        Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)!!

val BASE_VINE_HREF_URL = "/v"

fun Activity.hideKeypad() {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Hide keypad if visible
    if (this.currentFocus != null) {
        inputManager.hideSoftInputFromWindow(
                this.currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun String.hostName(): String? {
    var host: String? = null;
    try {
        host = (URL(this)).host
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return host
}

fun String.getPostUrl(): String {
    var url = ""
    val matcher = urlPattern.matcher(this)

    while (matcher.find()) {
        val matchStart = matcher.start(1)
        val matchEnd = matcher.end()
        // now you have the offsets of a URL match
        url = this.substring(matchStart, matchEnd)
    }
    return url;
}

fun String.readJsonFromUrl(): JSONObject? {
    val ins = URL(this).openStream()
    try {
        val rd = BufferedReader(InputStreamReader(ins, Charset.forName("UTF-8")))
        val jsonText = readAll(rd)
        return JSONObject(jsonText)
    } finally {
        ins.close()
    }
}

fun String.formatUrl(): String {
    return if(this.indexOf("?") >= 0) {
        this.substring(0, this.indexOf("?"))
    } else {
        this
    }
}

fun String.getFileSize(): Int {
    var conn: HttpURLConnection? = null
    try {
        conn = URL(this).openConnection() as HttpURLConnection
        conn.requestMethod = "HEAD"
        conn.getInputStream()
        return conn.getContentLength()
    } catch (e: IOException) {
        return -1
    } finally {
        assert(conn != null)
        conn!!.disconnect()
    }
}

fun String.contentType(): String = this.substring(this.lastIndexOf("/") + 1)

fun String.fileName(): String = this.substring(this.lastIndexOf("/") + 1)

fun String.directory(): String = this.substring(0, this.lastIndexOf("/"))

fun String.noExtension(): String = this.substring(0, this.lastIndexOf("."))

fun String.extension(): String {
    val ext = this.substring(this.lastIndexOf(".") + 1).split("?")
    return ext[0];
}

fun String.resolution(): String {
    val output1 = this.substring(this.indexOf("/"), this.lastIndexOf("/"))

    return output1.substring(output1.lastIndexOf("/") + 1)
}

fun String.genFileName(): String
        = String.format("TVD-%s.%s", UUID.randomUUID().toString().replace(".", ""), this)

fun String.toTitleCase(): String {
    val titleCase = StringBuilder()
    var nextTitleCase = true

    for (c in this.toCharArray()) {
        var value = c;
        if (Character.isSpaceChar(c)) {
            nextTitleCase = true
        } else if (nextTitleCase) {
            value = Character.toTitleCase(c)
            nextTitleCase = false
        }

        titleCase.append(value)
    }

    return titleCase.toString()
}

fun String.isValidVineHref(): Boolean {
    if(TextUtils.isEmpty(this)) return false

    if(this.contains("/")) {
        val baseUrl = this.substring(0, this.lastIndexOf("/"))
        return (baseUrl == BASE_VINE_HREF_URL)
    }

    return false
}

fun String.copyTextToClipboard(context: Context) {
    Log.v("Extension", this)
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("url", this)
    clipboardManager.primaryClip = clipData
}

fun File.duration(): Long {
    if(this.exists()) {
        var retriever: MediaMetadataRetriever? = null;
        try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(this.path);

            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            if(time != null) {
                return time.toLong()
            }
        } catch (ex: Exception){
            ex.printStackTrace()
        } finally {
            if (retriever != null) {
                retriever.release();
            }
        }
    }
    return 0L
}

fun uniqueInt(): Int = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

@Throws(IOException::class)
fun readAll(rd: Reader): String {
    val sb = StringBuilder()
    var cp: Int? = rd.read()

    while (cp != -1) {
        sb.append(cp?.toChar())

        cp = rd.read()
    }

    return sb.toString()
}
package com.japharr.videoplayer.data.model

import java.io.Serializable
import java.util.*

/**
 * Created by Japharr on 8/7/2018.
 */
data class Playlist(
        var videoUrl: Array<String>,
        var videoTitle: Array<String>,
        var videoType: Array<String>,
        var videoId: Array<String?>?
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (!Arrays.equals(videoUrl, other.videoUrl)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(videoUrl)
    }

}
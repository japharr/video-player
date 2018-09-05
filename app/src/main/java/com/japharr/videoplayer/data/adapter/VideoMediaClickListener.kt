package com.japharr.videoplayer.data.adapter

import android.view.View
import com.japharr.videoplayer.data.domain.VideoMedia

/**
 * Created by Japharr on 8/6/2018.
 */
interface VideoMediaClickListener {
    fun onClick(videoMedia: VideoMedia, position: Int)
    fun onOverviewClick(view: View, videoMedia: VideoMedia, position: Int)
}
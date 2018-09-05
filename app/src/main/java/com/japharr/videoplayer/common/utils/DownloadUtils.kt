package com.japharr.videoplayer.common.utils

import android.content.Context
import android.content.Intent
import com.japharr.videoplayer.Constants
import com.japharr.videoplayer.common.extension.contentType
import com.japharr.videoplayer.common.extension.noExtension
import com.japharr.videoplayer.data.domain.VideoMedia
import com.japharr.videoplayer.data.model.Playlist
import com.japharr.videoplayer.ui.player.activity.VideoPlayerActivity
import java.io.File

/**
 * Created by Japharr on 8/7/2018.
 */
class DownloadUtils {
    companion object Factory {
        val TAG = DownloadUtils::class.java.simpleName

        fun openVideoPlayer(context: Context, vararg selDownloads: VideoMedia) {
            if(selDownloads.isNotEmpty()) {
                val videoIds: MutableList<String?>? = mutableListOf<String?>()
                val videoUrls: MutableList<String> = mutableListOf<String>()
                val videoTypes: MutableList<String> = mutableListOf<String>()
                val videoTitles: MutableList<String> = mutableListOf<String>()
                for(download in selDownloads) {
                    val file = File(download.filePath)

                    if(file.exists()) {
                        videoIds?.add(download.uid)
                        videoUrls.add(file.path)
                        videoTypes.add("others")
                        videoTitles.add(download.fileName?.noExtension()!!)
                    }
                }

                if(videoUrls.isNotEmpty()) {
                    val playlist = Playlist(videoUrls.toTypedArray(),
                            videoTitles.toTypedArray(), videoTypes.toTypedArray(), videoIds?.toTypedArray())

                    val intent = Intent(context, VideoPlayerActivity::class.java)
                    intent.putExtra(VideoPlayerActivity.PLAYLIST, playlist)
                    context.startActivity(intent)

                    /*
                    if (mActionMode != null) {
                        mActionMode?.finish();
                    }*/
                }
            }
        }

    }
}
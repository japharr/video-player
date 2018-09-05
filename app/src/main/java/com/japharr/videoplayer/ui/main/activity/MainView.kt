package com.japharr.videoplayer.ui.main.activity

import com.japharr.videoplayer.data.domain.VideoMedia
import com.japharr.videoplayer.ui.BaseView

/**
 * Created by Japharr on 8/6/2018.
 */
interface MainView: BaseView {
    fun onLoadVideoSuccess(it: List<VideoMedia>)
    fun onLoadVideoFailed(it: Throwable)
    fun showLoadingView()

    fun onInitRecyclerView()
    fun onInitGridRecyclerView()
}
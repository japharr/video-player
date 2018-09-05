package com.japharr.videoplayer.ui.main.activity

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.japharr.videoplayer.R
import com.japharr.videoplayer.common.extension.contentType
import com.japharr.videoplayer.common.extension.directory
import com.japharr.videoplayer.common.extension.extension
import com.japharr.videoplayer.common.extension.fileName
import com.japharr.videoplayer.data.domain.VideoMedia
import com.japharr.videoplayer.data.repository.VideoMediaRepository
import com.japharr.videoplayer.ui.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Japharr on 8/6/2018.
 */
class MainPresenter @Inject constructor(val videoMediaRepository: VideoMediaRepository, val context: Context): BasePresenter<MainView>() {
    override val TAG = MainPresenter::class.java.simpleName

    fun initRecyclerView() {
        if(isListOrGrid()) {
            view?.onInitRecyclerView()
        } else {
            view?.onInitGridRecyclerView()
        }
    }

    fun isListOrGrid(): Boolean {
        val defPref = PreferenceManager.getDefaultSharedPreferences(context)
        return defPref.getBoolean(context.getString(R.string.key_select_list_view), true)
    }

    fun persistItemView() {
        val defPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = defPref.edit()
        editor.putBoolean(context.getString(R.string.key_select_list_view), !isListOrGrid())
        editor.apply()
    }

    fun loadAllVideos() {
        view?.showLoadingView()

        disposables.add(
                videoMediaRepository.findAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { view?.onLoadVideoSuccess(it) },
                                { view?.onLoadVideoFailed(it) }
                        )
        )
    }

    fun persistVideos(vararg filePath: String) {
        Log.v(TAG, "filePath: " + filePath.size)
        disposables.add(
                videoMediaRepository.findAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { afterLoading(it, *filePath) },
                                { view?.onLoadVideoFailed(it) }
                        )
        )
    }

    private fun afterLoading(it: List<VideoMedia>, vararg filePath: String) {
        Log.v(TAG, "afterLoading: " + filePath.size)
        Log.v(TAG, "afterLoading: " + it.size)

        val deleteList = ArrayList<VideoMedia>()

        it.filterNotTo(deleteList) { filePath.contains(it.filePath) }

        videoMediaRepository.deleteAll(*deleteList.toTypedArray())

        val createList = ArrayList<String>()
        for(path in filePath) {
            //val selectedMonth: VideoMedia? = it.single { s -> s.filePath == path }
            if(it.isNotEmpty()) {
                val selectedMonth: VideoMedia? = it.filter { s -> s.filePath == path }.firstOrNull()

                if(selectedMonth == null) {
                    createList.add(path)
                }
            } else {
                createList.add(path)
            }
        }

        val createMedias = createList.map {
            VideoMedia(0, UUID.randomUUID().toString(), it.fileName(),
                    it.extension(), it, it.directory(), Date(), Date(),
                    0, null)
        }

        videoMediaRepository.insertAll(*createMedias.toTypedArray())
    }
}
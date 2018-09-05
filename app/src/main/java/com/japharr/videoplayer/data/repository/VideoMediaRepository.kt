package com.japharr.videoplayer.data.repository

import com.japharr.videoplayer.data.domain.VideoMedia
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by Japharr on 8/5/2018.
 */
interface VideoMediaRepository {
    fun findById(id: Long): Maybe<VideoMedia>
    fun findByUid(uid: String): Maybe<VideoMedia?>
    fun findByPath(path: String): Maybe<VideoMedia?>
    fun findAll(): Flowable<List<VideoMedia>>
    fun count(): Int
    fun insert(videoMedia: VideoMedia)
    fun update(videoMedia: VideoMedia)
    fun insertAll(vararg videoMedias: VideoMedia)
    fun delete(videoMedia: VideoMedia)
    fun deleteAll(vararg videoMedias: VideoMedia)
}
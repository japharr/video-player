package com.japharr.videoplayer.data.repository

import com.japharr.videoplayer.data.dao.VideoMediaDao
import com.japharr.videoplayer.data.domain.VideoMedia
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.util.concurrent.Executor
import javax.inject.Inject

/**
 * Created by Japharr on 8/5/2018.
 */
class VideoMediaRepositoryImpl @Inject constructor(var videoMediaDao: VideoMediaDao, var executor: Executor): VideoMediaRepository {
    override fun findById(id: Long): Maybe<VideoMedia> {
        return videoMediaDao.findById(id)
    }

    override fun findByUid(uid: String): Maybe<VideoMedia?> {
        return videoMediaDao.findByUid(uid)
    }

    override fun findAll(): Flowable<List<VideoMedia>> {
        return videoMediaDao.getAll()
    }

    override fun findByPath(path: String): Maybe<VideoMedia?> {
        return videoMediaDao.findByPath(path)
    }

    override fun count(): Int {
        return videoMediaDao.count()
    }

    override fun insert(videoMedia: VideoMedia) {
        executor.execute { videoMediaDao.insert(videoMedia) }
    }

    override fun update(videoMedia: VideoMedia) {
        executor.execute { videoMediaDao.update(videoMedia) }
    }

    override fun insertAll(vararg videoMedias: VideoMedia) {
        executor.execute { videoMediaDao.insertAll(*videoMedias) }
    }

    override fun delete(videoMedia: VideoMedia) {
        executor.execute { videoMediaDao.delete(videoMedia) }
    }

    override fun deleteAll(vararg videoMedias: VideoMedia) {
        executor.execute { videoMediaDao.delete(*videoMedias) }
    }
}
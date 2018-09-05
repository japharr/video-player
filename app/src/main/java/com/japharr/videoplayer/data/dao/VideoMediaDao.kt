package com.japharr.videoplayer.data.dao

import android.arch.persistence.room.*
import com.japharr.videoplayer.data.domain.VideoMedia
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by Japharr on 8/5/2018.
 */
@Dao
interface VideoMediaDao {
    @Query("SELECT * FROM video where id = :id LIMIT 1")
    fun findById(id: Long): Maybe<VideoMedia>

    @Query("SELECT * FROM video where uid = :uid LIMIT 1")
    fun findByUid(uid: String): Maybe<VideoMedia?>

    @Query("SELECT * FROM video where file_path = :path LIMIT 1")
    fun findByPath(path: String): Maybe<VideoMedia?>

    @Query("SELECT * FROM video ORDER BY created_date DESC")
    fun getAll(): Flowable<List<VideoMedia>>

    @Query("SELECT COUNT(*) from video")
    fun count(): Int

    @Insert
    fun insert(download: VideoMedia): Long

    @Update
    fun update(download: VideoMedia): Int

    @Insert
    fun insertAll(vararg downloads: VideoMedia): List<Long>

    @Delete
    fun delete(vararg download: VideoMedia)
}
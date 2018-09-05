package com.japharr.videoplayer.data.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Created by Japharr on 8/5/2018.
 */
@Entity(tableName = "video")
data class VideoMedia(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = "uid") var uid: String? = null,
        @ColumnInfo(name = "file_name") var fileName: String?,
        @ColumnInfo(name = "file_type") var fileType: String?,
        @ColumnInfo(name = "file_path") var filePath: String?,
        @ColumnInfo(name = "directory") var directory: String? = null,
        @ColumnInfo(name = "created_date") var createdDate: Date? = Date(),
        @ColumnInfo(name = "last_modified_date") var lastModifiedDate: Date? = Date(),
        @ColumnInfo(name = "duration") var duration: Long = 0,
        @ColumnInfo(name = "description") var description: String? = null
): Serializable
package com.japharr.videoplayer.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.japharr.videoplayer.data.dao.VideoMediaDao
import com.japharr.videoplayer.data.domain.VideoMedia

/**
 * Created by Japharr on 8/5/2018.
 */
@Database(entities = [VideoMedia::class], version = AppDatabase.VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun videoMediaDao(): VideoMediaDao
    //abstract fun petDao(): PetDao
}
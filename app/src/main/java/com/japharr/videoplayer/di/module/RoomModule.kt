package com.japharr.videoplayer.di.module

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import com.japharr.videoplayer.App
import com.japharr.videoplayer.Constants
import com.japharr.videoplayer.data.dao.VideoMediaDao
import com.japharr.videoplayer.data.db.AppDatabase
import com.japharr.videoplayer.data.repository.VideoMediaRepository
import com.japharr.videoplayer.data.repository.VideoMediaRepositoryImpl
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Singleton

/**
 * Created by Japharr on 8/5/2018.
 */
@Module
class RoomModule() {
    private lateinit var appDatabase: AppDatabase

    constructor(app: App): this() {
        appDatabase = Room.databaseBuilder(app, AppDatabase::class.java, Constants.APP_DATABASE_NAME)
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
    }

    /*
    companion object {
        val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE download "
                        + " ADD COLUMN resolution TEXT");
            }
        }

        val MIGRATION_2_3 = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE download "
                        + " ADD COLUMN uid TEXT");
            }
        }
    }*/

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase = appDatabase

    @Singleton
    @Provides
    fun providesVideoMediaDao(appDatabase: AppDatabase): VideoMediaDao = appDatabase.videoMediaDao()

    @Singleton
    @Provides
    fun providesVideoMediaRepository(downloadDao: VideoMediaDao, executor: Executor):
            VideoMediaRepository = VideoMediaRepositoryImpl(downloadDao, executor)
}
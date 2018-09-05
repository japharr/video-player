package com.japharr.videoplayer.data.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import java.util.*

/**
 * Created by Japharr on 8/5/2018.
 */
class Converters {
    var gson: Gson = Gson()

    @TypeConverter fun dateFromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)
    @TypeConverter fun dateToTimestamp(date: Date?): Long? = date?.time

}
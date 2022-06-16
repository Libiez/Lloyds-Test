package com.lloyds.dictionary.dictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lloyds.dictionary.dictionary.data.local.entity.WordInfoEntity

@Database(
    entities = [WordInfoEntity::class],
    version = 3
)

@TypeConverters(Converters::class)
abstract class  WordInfoDatabase :RoomDatabase() {

    abstract val dao: WordInfoDao

}
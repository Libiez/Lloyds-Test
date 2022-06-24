package com.lloyds.dictionary.dictionary.di

import android.app.Application
import androidx.room.Room
import com.lloyds.dictionary.dictionary.data.local.Converters
import com.lloyds.dictionary.dictionary.data.local.WordInfoDatabase
import com.lloyds.dictionary.dictionary.data.remote.DictionaryApi
import com.lloyds.dictionary.dictionary.data.repository.WordInfoRepositoryImpl
import com.lloyds.dictionary.dictionary.data.util.GsonParser
import com.lloyds.dictionary.dictionary.domain.repository.WordInfoRepository
import com.lloyds.dictionary.dictionary.domain.use_cases.GetWordInfo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCases(repository: WordInfoRepository): GetWordInfo {
        return GetWordInfo(repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(db: WordInfoDatabase, api: DictionaryApi): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app: Application): WordInfoDatabase {
        return Room.databaseBuilder(
            app, WordInfoDatabase::class.java, "word_db"
        ).addTypeConverter(Converters(GsonParser(Gson()))).build()
    }

    @Singleton
    @Provides
    fun provideWordInfoDao(database: WordInfoDatabase) = database.dao

}
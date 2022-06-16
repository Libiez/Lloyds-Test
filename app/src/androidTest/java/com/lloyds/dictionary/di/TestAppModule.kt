package com.lloyds.dictionary.di

import android.content.Context
import androidx.room.Room
import com.lloyds.dictionary.dictionary.data.local.Converters
import com.lloyds.dictionary.dictionary.data.local.WordInfoDatabase
import com.lloyds.dictionary.dictionary.data.preferences.UserPreferences
import com.lloyds.dictionary.dictionary.data.remote.DictionaryApi
import com.lloyds.dictionary.dictionary.data.repository.WordInfoRepositoryImpl
import com.lloyds.dictionary.dictionary.data.util.GsonParser
import com.lloyds.dictionary.dictionary.domain.repository.WordInfoRepository
import com.lloyds.dictionary.dictionary.domain.use_cases.GetWordInfo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context)=
        Room.inMemoryDatabaseBuilder(context, WordInfoDatabase::class.java)
            .addTypeConverter(Converters(GsonParser(Gson())))
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideUserPreference(@ApplicationContext context: Context)=
       UserPreferences(context)

    @Provides
    fun provideGetWordInfoUseCases(repository: WordInfoRepository): GetWordInfo {
        return GetWordInfo(repository)
    }

    @Provides
    fun provideWordInfoRepository(db: WordInfoDatabase,api: DictionaryApi): WordInfoRepository {
        return  WordInfoRepositoryImpl(api,db.dao)
    }



}
package com.lloyds.dictionary.dictionary.di

import android.app.Application
import androidx.room.Room
import com.lloyds.dictionary.BuildConfig
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCases(repository: WordInfoRepository): GetWordInfo{
        return GetWordInfo(repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(db: WordInfoDatabase,api: DictionaryApi): WordInfoRepository{
        return  WordInfoRepositoryImpl(api,db.dao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app:Application): WordInfoDatabase  {
        return  Room.databaseBuilder(
            app, WordInfoDatabase::class.java,"word_db"
        ).addTypeConverter(Converters(GsonParser(Gson()))).
        build()
    }

    @Singleton
    @Provides
    fun provideWordInfoDao(database: WordInfoDatabase) = database.dao


    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi{

        return  Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .client(OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().build())
            }
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }

            }.build()
        )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }


}
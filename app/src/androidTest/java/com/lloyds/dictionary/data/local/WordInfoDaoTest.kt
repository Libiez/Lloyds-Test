package com.lloyds.dictionary.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lloyds.dictionary.dictionary.data.local.WordInfoDao
import com.lloyds.dictionary.dictionary.data.local.WordInfoDatabase
import com.lloyds.dictionary.dictionary.data.local.entity.WordInfoEntity
import com.lloyds.dictionary.dictionary.di.WordInfoModule
import com.lloyds.dictionary.dictionary.domain.model.Definition
import com.lloyds.dictionary.dictionary.domain.model.Meaning
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(WordInfoModule::class)
class WordInfoDaoTest {

    @get:Rule var hitRule = HiltAndroidRule(this)

     @Inject
     @Named("test_db")
     lateinit var dataBase: WordInfoDatabase
     private lateinit var dao: WordInfoDao

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        hitRule.inject()
        dao = dataBase.dao
        dataBase = Room.inMemoryDatabaseBuilder(context, WordInfoDatabase::class.java).build()
        dao = dataBase.dao
    }

    @After
    fun teardown(){
        dataBase.close()
    }

    @Test
    fun insertWordInfo() = runTest{

        val definitions = mutableListOf<Definition>()
        for(i in 1..5){
            definitions.add(Definition(definition = "institution $i", example = "bloodBank $i",synonyms=null, antonyms = null))
        }
        val meaning1 =Meaning(definitions,"noun")
        val meaning2 =Meaning(definitions,"noun")
        val meanings: List<Meaning> = listOf(meaning1,meaning2)
        val wordInfoEntity=WordInfoEntity(meanings,"bæŋk","bank")

        val wordInfoEntities  = listOf(wordInfoEntity)

        dao.insertWordInfo(wordInfoEntities)

        val allWordInfos =dao.getWordsInfo("bank")
        Assert.assertEquals(allWordInfos[0].word ,wordInfoEntity.word)

    }

    @Test
    fun deleteWordInfo() = runTest{

        val definitions = mutableListOf<Definition>()
        for(i in 1..5){
            definitions.add(Definition(definition = "institution $i", example = "bloodBank $i",synonyms=null, antonyms = null))
        }
        val meaning1 =Meaning(definitions,"noun")
        val meaning2 =Meaning(definitions,"noun")
        val meanings: List<Meaning> = listOf(meaning1,meaning2)
        val wordInfoEntity=WordInfoEntity(meanings,"bæŋk","bank")
        val wordInfoEntities  = listOf(wordInfoEntity)
        dao.insertWordInfo(wordInfoEntities)
        val allWordInfos =dao.getWordsInfo("bank")
        dao.deleteWordInfo(allWordInfos.map { it.word })

        Assert.assertEquals(allWordInfos[0].word ,wordInfoEntity.word)

    }


}
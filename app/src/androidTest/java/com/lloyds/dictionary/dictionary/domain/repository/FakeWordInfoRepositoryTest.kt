package com.lloyds.dictionary.dictionary.domain.repository

import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.domain.model.Definition
import com.lloyds.dictionary.dictionary.domain.model.Meaning
import com.lloyds.dictionary.dictionary.domain.model.WordInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWordInfoRepositoryTest: WordInfoRepository{

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> {
        return wordFlow
    }

     var wordFlow: Flow<Resource<List<WordInfo>>> = flow{
        emit(Resource.Success(data =returnCustomList()))
        delay(3000L)
    }


    private fun returnCustomList(): List<WordInfo> {

        val definitions = mutableListOf<Definition>()
        for (i in 1..5) {
            definitions.add(
                Definition(
                    definition = "institution $i",
                    example = "bloodBank $i",
                    synonyms = null,
                    antonyms = null
                )
            )
        }
        val meaning1 = Meaning(definitions, "noun")
        val meaning2 = Meaning(definitions, "noun")
        val meanings: List<Meaning> = listOf(meaning1, meaning2)

        val wordInfoEntity = WordInfo(meanings, "bæŋk", "bank")

        return listOf(wordInfoEntity)
    }

}
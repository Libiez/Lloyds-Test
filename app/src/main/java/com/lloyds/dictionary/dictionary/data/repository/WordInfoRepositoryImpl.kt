package com.lloyds.dictionary.dictionary.data.repository

import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.data.local.WordInfoDao
import com.lloyds.dictionary.dictionary.data.remote.DictionaryApi
import com.lloyds.dictionary.dictionary.domain.model.WordInfo
import com.lloyds.dictionary.dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl (
    private val api: DictionaryApi,
    private val dao: WordInfoDao
    ):WordInfoRepository {


    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {

        emit(Resource.Loading())

        val wordInfos= dao.getWordsInfo(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))

        try {

            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfo(remoteWordInfos.map { it.word })
            dao.insertWordInfo(remoteWordInfos.map { it.toWordInfoEntity() })


        }catch (e: HttpException){
            emit(Resource.Error(
                message = "Oops, Something went wrong "
            ))
        }catch (e: IOException){
            emit(Resource.Error(
                message = " Couldn't reach the network, Please check you network connection"
            ))
        }

        val newWordInfos= dao.getWordsInfo(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))



    }


}
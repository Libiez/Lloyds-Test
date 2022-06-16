package com.lloyds.dictionary.dictionary.domain.repository

import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

   fun getWordInfo(word:String) : Flow<Resource<List<WordInfo>>>

}
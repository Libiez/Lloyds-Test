package com.lloyds.dictionary.dictionary.domain.use_cases

import com.lloyds.dictionary.core.util.Resource
import com.lloyds.dictionary.dictionary.domain.model.WordInfo
import com.lloyds.dictionary.dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfo(
    private val repository: WordInfoRepository
) {

    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        if (word.isBlank()) {
            return flow { }
        }
        return repository.getWordInfo(word)
    }

}
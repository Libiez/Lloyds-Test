package com.lloyds.dictionary.dictionary.presenation

import com.lloyds.dictionary.dictionary.domain.model.WordInfo

data class WordInfoState(

    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false

)

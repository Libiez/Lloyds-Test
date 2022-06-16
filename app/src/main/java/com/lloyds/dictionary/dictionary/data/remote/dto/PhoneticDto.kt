package com.lloyds.dictionary.dictionary.data.remote.dto

import com.lloyds.dictionary.dictionary.domain.model.Phonetic

data class PhoneticDto(
    val audio: String,
    val sourceUrl: String,
    val text: String
) {
    fun toPhonetic(): Phonetic {
        return Phonetic(
            audio = audio,
            sourceUrl = sourceUrl,
            text = text
        )
    }

}
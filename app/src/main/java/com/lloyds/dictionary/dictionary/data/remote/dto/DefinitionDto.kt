package com.lloyds.dictionary.dictionary.data.remote.dto

import com.lloyds.dictionary.dictionary.domain.model.Definition

data class DefinitionDto(
    val antonyms: List<String>,
    val definition: String?,
    val synonyms: List<String>,
    val example: String?
) {
    fun toDefinition(): Definition {
        return Definition(
            definition = definition,
            antonyms = antonyms,
            synonyms = synonyms,
            example = example

        )
    }
}
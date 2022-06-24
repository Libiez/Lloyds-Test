package com.lloyds.dictionary.dictionary.presenation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lloyds.dictionary.dictionary.domain.model.WordInfo

@Composable
fun WordInfoItem(
    wordInfo: WordInfo,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        Text(
            text = wordInfo.word,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = wordInfo.phonetic ?: "",
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(16.dp))

        wordInfo.meanings.forEach { meaning ->

            Text(text = meaning.partOfSpeech, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Monospace)
            meaning.definitions.forEachIndexed { i, definition ->

                Text(text = "${i + 1}. ${definition.definition}",fontFamily = FontFamily.Monospace)
                Log.d("WordInfoItem:"," ${i + 1}. ${definition.definition}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Example: ${definition.example}",fontFamily = FontFamily.Monospace)
                Log.d("WordInfoItem:", "Example: ${definition.example}")

                Spacer(modifier = Modifier.height(8.dp))

            }

        }

    }


}
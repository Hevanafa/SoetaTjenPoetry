package com.hevanafa.soetatjenpoetry

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class RawPoemData (
    val poems: ArrayList<Poem>
)

data class Poem (
    val id: Int,
    val title: String,
    val poet: String,
    val image: String?,
    var parsedImage: Bitmap?,
    val verses: String,
    val datetime: String // GMT+7
)

data class State (
    val poems: ArrayList<Poem> = arrayListOf(),
    val activeId: Int = -1
)

class StateViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(State())
    val uiStateFlow = _uiState.asStateFlow()

    // minSdk should be set to 26
    // Ref: https://stackoverflow.com/questions/25938560/
    val input_format = DateTimeFormatter.ISO_DATE_TIME

    // Ref: https://stackoverflow.com/questions/1459656/
//    val output_format = SimpleDateFormat("dd-MM-yyyy HH:mm")
    val output_format = SimpleDateFormat("dd MMM yyyy", Locale.UK)

    @Composable
    fun loadPoems(context: Context) {
        try {
            val jsonStr = context.resources.openRawResource(R.raw.poems)
                .bufferedReader()
                .use { it.readText() }

            val parsed = Gson().fromJson(jsonStr, RawPoemData::class.java)

            parsed.poems.forEach { poem ->
                if (poem.image != null) {
                    poem.parsedImage = loadImage(poem.image)
                }
            }

            poems = parsed.poems
        } catch (ioex: IOException) {
            Log.e("LoadPoems", "Error when accessing poems.json: " + (ioex.localizedMessage ?: ""))
        }
    }

    fun loadImage(base64String: String): Bitmap? {
        return try {
            val bytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (ex: Exception) {
            println("Unable to parse base64 image: " + ex.localizedMessage)
            null
        }
    }

    var poems by mutableStateOf(arrayListOf<Poem>())
        private set

    var activePoem = mutableStateOf<Poem?>(null)
        get() = field
        set(value) { field = value }

    fun unsetActivePoem() {
        activePoem.value = null
    }

    @Composable
    fun getReadableDatetime(): String {
        val datetimeStr = activePoem.value?.datetime ?: ""
        if (datetimeStr.isEmpty())
            return ""

        val offsetDT = OffsetDateTime.parse(datetimeStr, input_format)
        val datetime = Date.from(Instant.from(offsetDT))
        return output_format.format(datetime)
    }

}

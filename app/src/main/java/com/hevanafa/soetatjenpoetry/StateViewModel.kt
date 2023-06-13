package com.hevanafa.soetatjenpoetry

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

data class RawPoemData (
    val poems: ArrayList<Poem>
)

data class Poem (
    val id: Int,
    val title: String,
    val poet: String,
    val image: String?,
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
    val output_format = SimpleDateFormat("dd MMM yyyy")

    fun setActiveId(newId: Int) {
        _uiState.update{
            it.copy(activeId = newId)
        }
    }

    @Composable
    fun loadPoems(context: Context) {
        try {
            val jsonStr = context.resources.openRawResource(R.raw.poems)
                .bufferedReader()
                .use { it.readText() }

            val parsed = Gson().fromJson(jsonStr, RawPoemData::class.java)
            _uiState.update {
                it.copy(poems = parsed.poems)
            }
        } catch (ioex: IOException) {
            Log.e("LoadPoems", "Error when accessing poems.json: " + (ioex.localizedMessage ?: ""))
        }
    }

    @Composable
    fun getPoems(): ArrayList<Poem> {
        return getState().poems
    }

    @Composable
    fun getState(): State {
        return uiStateFlow.collectAsState().value
    }

    // Todo: get the human-readable date & time

    @Composable
    fun getActiveId(): Int {
        return getState().activeId
    }

    fun unsetActivePoem() {
        _uiState.update {
            it.copy(activeId = -1)
        }
    }

    @Composable
    fun getActivePoem(): Poem? {
        return getState().poems.find { it.id == getState().activeId }
    }

    @Composable
    fun getActivePoetTitle(): String {
        return getActivePoem()?.title ?: ""
    }

    @Composable
    fun getReadableDatetime(): String {
        val datetimeStr = getActivePoem()?.datetime ?: ""
        if (datetimeStr.isEmpty())
            return ""

        val offsetDT = OffsetDateTime.parse(datetimeStr, input_format)
        val datetime = Date.from(Instant.from(offsetDT))
        return output_format.format(datetime)
    }

}

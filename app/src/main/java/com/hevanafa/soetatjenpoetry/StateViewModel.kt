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
        return getState().poems.find { it.id == getState().activeId }?.title ?: ""
    }

}

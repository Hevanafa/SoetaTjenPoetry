package com.hevanafa.soetatjenpoetry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    viewModel: StateViewModel,
    navController: NavController,
    canNavigateBack: Boolean,
    currentScreen: Screens
) {
    val activePoem = viewModel.activePoem.value

    TopAppBar(
        title = { Text(
            when (currentScreen) {
                Screens.PoemDetails -> activePoem?.title ?: ""
                Screens.Start -> "Steven de Dichter's Poetry Collection"
                else -> { currentScreen.name }
            }
        ) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    navController.navigateUp()
                    viewModel.unsetActivePoem()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

package com.hevanafa.soetatjenpoetry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.hevanafa.soetatjenpoetry.ui.theme.SoetaTjenPoetryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoetaTjenPoetryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScaffold()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BaseScaffold() {
        val viewModel: StateViewModel = viewModel()

        viewModel.loadPoems(applicationContext)

        // for navigation & in-app routing
        val navController: NavHostController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = Screens.valueOf(
            backStackEntry?.destination?.route ?: Screens.Start.name
        )
        val canNavigateBack = navController.previousBackStackEntry != null

        val activePoem: Poem? = viewModel.getActivePoem()

        Scaffold (topBar = {
            TopAppBar(
                title = { Text(
                    activePoem?.title ?: "Soeta Tjen's Poetry Collection"
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
        }) { innerPadding ->
            NavHost(navController, Screens.Start.name) {
                composable(Screens.Start.name) {
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        viewModel.getPoems().map {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clickable {
                                    viewModel.setActiveId(it.id)
                                    navController.navigate(Screens.PoemDetails.name)
                                }, colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)) {

                                Box(modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center) {
                                    Text(it.title)
                                }
                            }
                        }
                    }
                }

                composable(Screens.PoemDetails.name) {
                    Column (modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (activePoem != null) {
                            Text(activePoem.datetime)
                            Text( "By " + activePoem.poet, fontWeight = FontWeight.Bold)
                            Text(activePoem.verses)
                        } else {
                            Text("Sorry, this poem isn't ready at the moment!")
                        }
                    }
                }
            }
        }
    }
}

package com.hevanafa.soetatjenpoetry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    var viewModel: StateViewModel? = null

    // for navigation & in-app routing
    var navController: NavHostController? = null

    @Composable
    fun getCurrentScreen(): Screens {
        val backStackEntry by navController!!.currentBackStackEntryAsState()

        return Screens.valueOf(
            backStackEntry?.destination?.route ?: Screens.Start.name
        )
    }

    @Composable
    fun getCurrentRoute(): String {
        val backStackEntry by navController!!.currentBackStackEntryAsState()
        return backStackEntry?.destination?.route ?: Screens.Start.name
    }

    @Composable
    fun getCanNavigateBack(): Boolean {
        return navController!!.previousBackStackEntry != null && getCurrentRoute() != Screens.Start.name
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BaseScaffold() {
        viewModel = viewModel()
        viewModel!!.loadPoems(applicationContext)

        navController = rememberNavController()
        // val backStackEntry by navController!!.currentBackStackEntryAsState()
        // val currentRoute = getCurrentRoute()

        // navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener(navController, backStackEntry.first))

        Scaffold (topBar = {
            MainTopBar(
                navController = navController!!,
                viewModel = viewModel!!,
                canNavigateBack = getCanNavigateBack(),
                currentScreen = getCurrentScreen()
            )
        }) { innerPadding ->
            NavHost(navController!!, Screens.Start.name) {
                composable(Screens.Start.name) {
                    StartScreen(
                        navController = navController!!,
                        viewModel = viewModel!!,
                        baseModifier = Modifier.padding(innerPadding)
                    )
                }

                composable(Screens.PoemDetails.name) {
                    PoemDetailsView(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        viewModel = viewModel!!
                    )
                }
            }
        }
    }




}

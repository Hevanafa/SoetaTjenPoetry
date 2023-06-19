package com.hevanafa.soetatjenpoetry

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
    @Composable
    fun BaseScaffold() {
        viewModel = viewModel()
        viewModel!!.loadPoems(applicationContext)

        navController = rememberNavController()
        val backStackEntry by navController!!.currentBackStackEntryAsState()
        val currentRoute = getCurrentRoute()

//        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener(navController, backStackEntry.first))

        val poems = viewModel!!.getPoems()
        val activePoem: Poem? = viewModel!!.getActivePoem()

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
                        baseModifier = Modifier.padding(innerPadding),
                        poems = poems
                    )
                }

                composable(Screens.PoemDetails.name) {
                    PoemDetailsView(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        viewModel = viewModel!!,
                        activePoem = activePoem
                    )
                }
            }
        }
    }



    @Composable
    fun PoemDetailsView(
        modifier: Modifier,
        viewModel: StateViewModel,
        activePoem: Poem?
    ) {
        Column (modifier = modifier.absolutePadding(left = 10.dp, right = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (activePoem != null) {
                if (activePoem.parsedImage != null) {
                    Image(
                        bitmap = activePoem.parsedImage!!.asImageBitmap(),
                        contentDescription = "Image for " + activePoem.title)
                }

                Text(viewModel.getReadableDatetime())
                Text( "By " + activePoem.poet, fontWeight = FontWeight.Bold)
                Text(activePoem.verses)
            } else {
                Text("Sorry, this poem isn't ready at the moment!")
            }
        }
    }
}

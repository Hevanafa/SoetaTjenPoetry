package com.hevanafa.soetatjenpoetry

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalTextApi::class)
@Composable
fun StartScreen(
    navController: NavHostController,
    viewModel: StateViewModel,
    baseModifier: Modifier
) {
    val poems = viewModel.poems

    if (poems.isEmpty())
        Column(modifier = baseModifier ) {
            Text("This list is empty.  Make sure poems.json has been generated & included in res\\raw.")
        }

    LazyVerticalGrid(
        modifier = baseModifier
            .absolutePadding(left = 10.dp, right = 10.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            count = poems.size,
            key = { poems[it].id }
        ) { idx ->
            val poem = remember { poems[idx] }

            Card(modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clickable {
//                    viewModel.setActiveId(poem.id)
                    viewModel.activePoem.value = poem // .setActivePoem(poem)
                    navController.navigate(Screens.PoemDetails.name)
                }, colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    val imageModifier = Modifier.fillMaxWidth()

                    if (poem.parsedImage != null) {
                        val bitmap = remember {poem.parsedImage!!.asImageBitmap()}

                        Image(
                            modifier = imageModifier,
                            bitmap = bitmap,
                            contentDescription = poem.title
                        )
                    } else {
                        Image(
                            modifier = imageModifier,
                            painter = painterResource(id = R.drawable.poem_no_image),
                            contentDescription = poem.title
                        )
                    }

                    Text(poem.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                }


            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun StartScreenLegacy(
    navController: NavHostController,
    viewModel: StateViewModel,
    poems: ArrayList<Poem>,
    baseModifier: Modifier
) {
    if (poems.isEmpty())
        Column(modifier = baseModifier ) {
            Text("This list is empty.  Make sure poems.json has been generated & included in res\\raw.")
        }

    LazyVerticalGrid(
        modifier = baseModifier
            .absolutePadding(left = 10.dp, right = 10.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            count = poems.size,
            key = { poems[it].id }) { idx ->

            val poem = poems[idx]

            Card(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    viewModel!!.setActiveId(poem.id)
                    navController!!.navigate(Screens.PoemDetails.name)
                }, colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (poem.parsedImage != null) {
                        Image(
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            bitmap = poem.parsedImage!!.asImageBitmap(),
                            contentDescription = poem.title
                        )

                        Text(
                            poem.title,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp),
                            color = Color.White,
                            style = TextStyle.Default.copy(
                                fontSize = 20.sp,
                                color = Color.Black,
                                drawStyle = Stroke(
                                    miter = 10f,
                                    width = 2f,
                                    join = StrokeJoin.Round
                                )
                            )
                        )
                    } else {
                        Text(poem.title)
                    }
                }
            }
        }
    }
}
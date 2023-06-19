package com.hevanafa.soetatjenpoetry

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

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

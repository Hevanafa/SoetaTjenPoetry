package com.hevanafa.soetatjenpoetry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
            } else {
                Image(
                    painter = painterResource(id = R.drawable.poem_no_image),
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
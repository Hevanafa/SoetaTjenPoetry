package com.hevanafa.soetatjenpoetry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PoemDetailsView(
    modifier: Modifier,
    viewModel: StateViewModel
) {
//    println("Test rerender")
    val activePoem = remember { viewModel.activePoem.value }

    Column (modifier = modifier.absolutePadding(left = 10.dp, right = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (activePoem != null) {
            val contentDescription = remember { "Image for " + activePoem.title }
            val readableDatetime = viewModel.getReadableDatetime()

            print("Test rerender w/ active poem")

            if (activePoem.parsedImage != null) {
                val bitmap = remember { activePoem.parsedImage!!.asImageBitmap() }
                Image(
                    bitmap = bitmap,
                    contentDescription = contentDescription
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.poem_no_image),
                    contentDescription = contentDescription)
            }

            Text(readableDatetime)
            Text( "By " + activePoem.poet, fontWeight = FontWeight.Bold)
            Text(activePoem.verses)
        } else {
            Text("Sorry, this poem isn't ready at the moment!")
        }
    }
}
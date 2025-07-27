package com.asadbyte.codeapp.ui.generator.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.ItimFont

@Composable
fun WifiInputScreen(modifier: Modifier = Modifier) {

}

@Composable
fun WifiInputCard(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun GenerateInputPreview() {
    val cardData = SimpleInputCardData(
        imageRes = R.drawable.ic_input_text,
        title = "Text",
        placeholder = "Enter text"
    )
    CodeAppTheme {
        Column(
            modifier = Modifier.fillMaxSize().background(Gray10)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_detail_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {  }
                )
                Text(
                    text = "cardData.title",
                    color = Color.White,
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            Spacer(modifier = Modifier.size(120.dp))
            SimpleInputCard(
                textValue = "text",
                onValueChange = {  },
                cardData = cardData,
                onGenerateClick = {},
                modifier = Modifier
                    .height(336.5.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

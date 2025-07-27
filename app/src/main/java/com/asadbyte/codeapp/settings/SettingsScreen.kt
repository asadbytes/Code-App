package com.asadbyte.codeapp.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.Gray80
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var isVibrate by remember { mutableStateOf(false) }
    var isBeep by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.qrcode_bg),
            contentDescription = "background image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().scale(scaleX = 1.5f, scaleY = 1.5f)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(110.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.ic_detail_back),
                    contentDescription = "Back button",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxSize()
                        .clickable { onNavigateBack() }
                )
            }
            Text(
                text = "Settings",
                color = MyYellow,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 10.dp, bottom = 10.dp)
            )
            SettingsCard(
                imageRes = R.drawable.ic_settings_vibrate,
                title = "Vibrate",
                subtitle = "Vibration when scan is done",
                showSwitch = true,
                switchChecked = isVibrate,
                onCheckedChange = { isVibrate = it }
            )
            SettingsCard(
                imageRes = R.drawable.ic_settings_beep,
                title = "Beep",
                showSwitch = true,
                switchChecked = isBeep,
                subtitle = "Beep when scan is done",
                onCheckedChange = { isBeep = it }
            )

            Text(
                text = "Support",
                color = MyYellow,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 10.dp, bottom = 10.dp, top = 40.dp)
            )
            SettingsCard(
                imageRes = R.drawable.ic_settings_rate,
                title = "Rate Us",
                subtitle = "Your best reward to us"
            )
            SettingsCard(
                imageRes = R.drawable.ic_settings_privacy,
                title = "Privacy Policy",
                subtitle = "Read our privacy policy"
            )
            SettingsCard(
                imageRes = R.drawable.ic_settings_share,
                title = "Share",
                subtitle = "Share with your friends"
            )
        }
    }
}

@Composable
fun SettingsCard(
    imageRes: Int,
    title: String,
    subtitle: String,
    showSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val yStrokePosition = if(showSwitch) 168f else 168f
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(Gray30)
                .padding(16.dp)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val color = MyYellow
                    drawLine(
                        color = color,
                        start = Offset(0f-26f, yStrokePosition),
                        end = Offset(size.width + 26, yStrokePosition),
                        strokeWidth = strokeWidth
                    )
                }
        ){
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = subtitle,
                    color = Color.White,
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if(showSwitch) {
                Switch(
                    checked = switchChecked,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(horizontal = 10.dp),
                    colors = SwitchDefaults.colors(
                        // Off state colors
                        uncheckedTrackColor = Gray30,
                        uncheckedThumbColor = MyYellow, // Thumb color in off state (or your preferred color)
                        uncheckedBorderColor = MyYellow,

                        // On state colors
                        checkedTrackColor = MyYellow,
                        checkedThumbColor = Gray30,
                        checkedBorderColor = MyYellow// Makes border invisible in on state
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    SettingsScreen(
        onNavigateBack = {}
    )
}

package com.asadbyte.codeapp.ui.settings

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.Gray80
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow
import androidx.core.net.toUri

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    settingViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by settingViewModel.settingsUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val privacyPolicyUrl = "https://www.freeprivacypolicy.com/live/1efb0d1f-0be6-4cd1-82a8-9479c6243c3b"
    val playStoreUrl = "https://play.google.com/store/apps/details?id=com.asadbyte.codeapp"

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
                .verticalScroll(rememberScrollState()) // Added scrolling
                .padding(horizontal = 16.dp) // Consistent horizontal padding
                .padding(top = 8.dp, bottom = 24.dp) // Top and bottom padding
        ) {
            // Back button with better sizing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp) // Standard touch target size
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back_no_bg),
                        contentDescription = "Back button",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Settings title
            Text(
                text = "Settings",
                color = MyYellow,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
            )

            // Settings cards with consistent spacing
            SettingsCard(
                imageRes = R.drawable.ic_settings_vibrate,
                title = "Vibrate",
                subtitle = "Vibration when scan is done",
                showSwitch = true,
                switchChecked = settingsUiState.vibration,
                onCheckedChange = { settingViewModel.setVibration(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsCard(
                imageRes = R.drawable.ic_settings_beep,
                title = "Beep",
                showSwitch = true,
                switchChecked = settingsUiState.beepSound,
                subtitle = "Beep when scan is done",
                onCheckedChange = { settingViewModel.setBeep(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Support section title
            Text(
                text = "Support",
                color = MyYellow,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
            )

            // Support cards
            SettingsCard(
                imageRes = R.drawable.ic_settings_rate,
                title = "Rate Us",
                subtitle = "Your best reward to us",
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, playStoreUrl.toUri())
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsCard(
                imageRes = R.drawable.ic_settings_privacy,
                title = "Privacy Policy",
                subtitle = "Read our privacy policy",
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, privacyPolicyUrl.toUri())
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsCard(
                imageRes = R.drawable.ic_settings_share,
                title = "Share",
                subtitle = "Share with your friends",
                modifier = Modifier.clickable {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Check out this awesome app!")
                        putExtra(Intent.EXTRA_TEXT, "Hey, try this app: https://play.google.com/store/apps/details?id=${context.packageName}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            )

            // Extra bottom spacing for better scrolling experience
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingsCard(
    imageRes: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    showSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp), // Slightly reduced elevation
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight() // Changed from fixed height to wrap content
            .clip(RoundedCornerShape(12.dp)) // Added rounded corners for modern look
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray30)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val color = MyYellow
                    // Bottom border
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(16.dp)
        ) {
            // Icon with consistent sizing
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp) // Fixed size for consistency
                    .padding(end = 16.dp)
            )

            // Text content with flexible width
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.8f), // Slightly transparent for hierarchy
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Switch with proper spacing
            if (showSwitch) {
                Switch(
                    checked = switchChecked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.padding(start = 8.dp),
                    colors = SwitchDefaults.colors(
                        // Off state colors
                        uncheckedTrackColor = Gray30,
                        uncheckedThumbColor = MyYellow,
                        uncheckedBorderColor = MyYellow,
                        // On state colors
                        checkedTrackColor = MyYellow,
                        checkedThumbColor = Gray30,
                        checkedBorderColor = MyYellow
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
                .verticalScroll(rememberScrollState()) // Added scrolling
                .padding(horizontal = 16.dp) // Consistent horizontal padding
                .padding(top = 8.dp, bottom = 24.dp) // Top and bottom padding
        ) {
            // Back button with better sizing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp) // Standard touch target size
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back_no_bg),
                        contentDescription = "Back button",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Settings title
            Text(
                text = "Settings",
                color = MyYellow,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
            )

            // Settings cards with consistent spacing
            SettingsCard(
                imageRes = R.drawable.ic_settings_vibrate,
                title = "Vibrate",
                subtitle = "Vibration when scan is done",
                showSwitch = true,
                switchChecked = true,
                onCheckedChange = { }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsCard(
                imageRes = R.drawable.ic_settings_beep,
                title = "Beep",
                showSwitch = true,
                switchChecked = false,
                subtitle = "Beep when scan is done",
                onCheckedChange = { }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Support section title
            Text(
                text = "Support",
                color = MyYellow,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
            )

            // Support cards
            SettingsCard(
                imageRes = R.drawable.ic_settings_rate,
                title = "Rate Us",
                subtitle = "Your best reward to us"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsCard(
                imageRes = R.drawable.ic_settings_privacy,
                title = "Privacy Policy",
                subtitle = "Read our privacy policy"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsCard(
                imageRes = R.drawable.ic_settings_share,
                title = "Share",
                subtitle = "Share with your friends"
            )

            // Extra bottom spacing for better scrolling experience
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

package com.asadbyte.codeapp.ui.adsMob

import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.setPadding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView as AdmobNativeAdView // Alias to avoid name clash

@Composable
fun NativeAdView(nativeAd: NativeAd, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = {
            // Create Views Programmatically
            val adIcon = ImageView(context)
            adIcon.id = 1 // Assign unique IDs for a potential state restoration

            val adHeadline = TextView(context).apply {
                id = 2
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setTextColor(Color.Black.toArgb())
                // You can also set typefaces here
            }

            val adBody = TextView(context).apply {
                id = 3
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setTextColor(Color.Gray.toArgb())
            }

            val adCallToAction = Button(context).apply {
                id = 4
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                // You would typically set a background drawable for the button
            }

            // Create a layout for the text elements (headline and body)
            val textLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(adHeadline)
                addView(adBody)
            }

            // Create the main layout for the ad content
            val adContentLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(16) // Use a dp-to-px conversion for better results
                gravity = Gravity.CENTER_VERTICAL

                // Add icon
                val iconParams = LinearLayout.LayoutParams(120, 120) // dp-to-px needed
                iconParams.marginEnd = 16
                addView(adIcon, iconParams)

                // Add text layout
                addView(textLayout, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ))
            }

            // Create the root AdmobNativeAdView
            val nativeAdView = AdmobNativeAdView(context).apply {
                // This is the root container
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                // Create final layout with CTA button below the content
                val finalLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(adContentLayout)

                    val ctaParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    ctaParams.topMargin = 16
                    addView(adCallToAction, ctaParams)
                }

                addView(finalLayout)

                // IMPORTANT: Register the views with the NativeAdView
                this.headlineView = adHeadline
                this.bodyView = adBody
                this.callToActionView = adCallToAction
                this.iconView = adIcon
            }

            nativeAdView
        },
        update = { nativeAdView ->
            // This block is called when the nativeAd state changes.
            // We find the views by their registered properties on the NativeAdView.
            val adHeadline = nativeAdView.headlineView as TextView
            val adBody = nativeAdView.bodyView as TextView
            val adCallToAction = nativeAdView.callToActionView as Button
            val adIcon = nativeAdView.iconView as ImageView

            // Populate the views with the ad content
            adHeadline.text = nativeAd.headline
            adBody.text = nativeAd.body
            adCallToAction.text = nativeAd.callToAction
            nativeAd.icon?.drawable?.let {
                adIcon.setImageDrawable(it)
            }

            // IMPORTANT: Associate the ad object with the view
            nativeAdView.setNativeAd(nativeAd)
        }
    )
}
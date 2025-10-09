package com.arkhe.menu.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.arkhe.menu.R
import com.arkhe.menu.presentation.ui.theme.ArkheTheme

@Composable
fun LoadingIndicatorSpinner(
    modifier: Modifier = Modifier,
    message: String = ""
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.loading_gray_spinner)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            val newColor = Color.Gray

            val dynamicProperties = rememberLottieDynamicProperties(
                rememberLottieDynamicProperty(
                    property = LottieProperty.COLOR_FILTER,
                    value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        newColor.hashCode(),
                        BlendModeCompat.SRC_ATOP
                    ),
                    keyPath = arrayOf("**")
                )
            )

            LottieAnimation(
                composition = composition,
                dynamicProperties = dynamicProperties,
                progress = { progress },
                modifier = Modifier.size(60.dp),
            )
            Text(
                text = "Loading $message ...",
                style = MaterialTheme.typography.bodyMedium,
                color = newColor
            )
        }
    }
}

@Composable
fun LoadingGraySpinner(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.loading_gray_spinner)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            val newColor = Color.Gray

            val dynamicProperties = rememberLottieDynamicProperties(
                rememberLottieDynamicProperty(
                    property = LottieProperty.COLOR_FILTER,
                    value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        newColor.hashCode(),
                        BlendModeCompat.SRC_ATOP
                    ),
                    keyPath = arrayOf("**")
                )
            )

            LottieAnimation(
                composition = composition,
                dynamicProperties = dynamicProperties,
                progress = { progress },
                modifier = Modifier.size(60.dp),
            )
        }
    }
}

@Composable
fun LoadingWaitingData(
    modifier: Modifier = Modifier,
    message: String = "No Data Available ...",
    isButtonLoad: Boolean = true,
    onLoad: () -> Unit = { }
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.loading_data_grid)
            )

            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "$message ...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            if (isButtonLoad) {
                Button(onClick = onLoad) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Load",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Load $message")
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun LoadingIndicatorSpinnerPreview() {
    ArkheTheme {
        LoadingIndicatorSpinner(
            modifier = Modifier,
            message = "Preview"
        )
    }
}*/

@Preview(showBackground = true)
@Composable
fun LoadingGraySpinnerPreview() {
    ArkheTheme {
        LoadingGraySpinner(
            modifier = Modifier
        )
    }
}
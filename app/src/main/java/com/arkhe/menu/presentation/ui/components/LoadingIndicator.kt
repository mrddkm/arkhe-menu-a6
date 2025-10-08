package com.arkhe.menu.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                LottieCompositionSpec.RawRes(R.raw.loading_ios_spinner)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "Loading $message ...",
                style = MaterialTheme.typography.bodyMedium
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

            val newColor = MaterialTheme.colorScheme.primary

            val dynamicProps = rememberLottieDynamicProperties(
                rememberLottieDynamicProperty(
                    property = LottieProperty.COLOR,
                    value = newColor.toArgb(),
                    keyPath = arrayOf(
                        "**",
                        "Stroke 1",
                        "Color"
                    )
                )
            )

            LottieAnimation(
                composition = composition,
                dynamicProperties = dynamicProps,
                modifier = Modifier.size(60.dp),
            )
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
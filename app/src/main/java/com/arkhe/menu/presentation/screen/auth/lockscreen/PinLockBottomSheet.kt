package com.arkhe.menu.presentation.screen.auth.lockscreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.presentation.ui.components.edit.AnimatedNumericKeypad
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.Constants.MaxMinLength.MAX_LENGTH_PIN
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.Close
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinLockBottomSheet(
    onDismiss: () -> Unit,
    onPinEntered: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.Lock,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onDismiss() }) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.Close,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        PinLockContent(
            onPinEntered = onPinEntered
        )
    }
}

@Composable
fun PinLockContent(
    state: PinLockState = rememberPinLockState(),
    langViewModel: LanguageViewModel = koinViewModel(),
    onPinEntered: (String) -> Unit
) {
    val labelPinEntered = "4-digit PIN"

    LaunchedEffect(state.pin) {
        if (state.pin.length == MAX_LENGTH_PIN) {
            onPinEntered(state.pin)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = langViewModel.getLocalized(Lang.PIN_LOCK),
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(4.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = labelPinEntered,
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray
            )
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                repeat(MAX_LENGTH_PIN) { index ->
                    val filled = index < state.pin.length
                    val scale by animateFloatAsState(
                        targetValue = if (filled) 1.2f else 1f,
                        animationSpec = spring(
                            dampingRatio = 0.4f,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "pinDotScale"
                    )
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .background(
                                if (filled) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        AnimatedNumericKeypad(
            onDigit = { digit ->
                state.onPinChange(state.pin + digit)
            },
            onDelete = {
                state.onPinChange(state.pin.dropLast(1))
            }
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {},
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Forgot PIN ?..",
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun rememberPinLockState(): PinLockState {
    var pin by remember { mutableStateOf("") }

    return PinLockState(
        pin = pin,
        onPinChange = { pin = it }
    )
}

data class PinLockState(
    val pin: String,
    val onPinChange: (String) -> Unit
)

@Preview(showBackground = true)
@Composable
fun PinLockBottomSheetPreview() {
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule,
                previewModule
            )
        }
    ) {
        ArkheTheme {
            PinLockContent(
                onPinEntered = { }
            )
        }
    }
}

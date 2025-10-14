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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arkhe.menu.presentation.ui.components.edit.AnimatedNumericKeypad
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.utils.Constants.MaxMinLength.MAX_LENGTH_PIN
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.ArrowIosBack
import compose.icons.evaicons.outline.CheckmarkCircle

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
        PinLockContent(
            onPinEntered = onPinEntered,
            onDismiss = onDismiss
        )
    }


    var pin by remember { mutableStateOf("") }
    val isValid = pin.length == 4

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Enter PIN", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        if (it.length <= 4 && it.all(Char::isDigit)) pin = it
                    },
                    label = { Text("4-digit PIN") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onPinEntered(pin) },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Unlock")
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    }
}

@Composable
fun PinLockContent(
    state: PinLockState = rememberPinLockState(),
    onPinEntered: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val labelPinLock = "Unlock Screen"
    val labelPinEntered = "4-digit PIN"
    val confirmingPin by remember(state.pin) {
        derivedStateOf { state.pin.length == MAX_LENGTH_PIN }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = labelPinLock, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onDismiss) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Cancel")
                }
            }
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = { onPinEntered(state.pin) },
                enabled = confirmingPin,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.CheckmarkCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Unlock")
                }
            }
        }
    }
}

@Composable
fun rememberPinLockState(): PinLockState {
    var pin by remember { mutableStateOf("") }

    return remember {
        PinLockState(
            pin = pin,
            onPinChange = { pin = it }
        )
    }
}

data class PinLockState(
    val pin: String,
    val onPinChange: (String) -> Unit
)

@Preview(showBackground = true)
@Composable
fun PinLockBottomSheetPreview() {
    ArkheTheme {
        PinLockContent(
            onPinEntered = { },
            onDismiss = { }
        )
    }
}

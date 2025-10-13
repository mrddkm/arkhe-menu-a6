package com.arkhe.menu.presentation.ui.components.edit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_MAIL
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_PHONE
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosDownward
import compose.icons.evaicons.outline.Calendar
import compose.icons.evaicons.outline.CloseCircle
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun EditNameField(
    value: String,
    label: String = "Name",
    onValueChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.uppercase()) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = MaterialTheme.shapes.medium,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = EvaIcons.Outline.CloseCircle,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@Composable
fun EditInitialFields(
    initial: String,
    labelInitial: String = "Initial",
    onInitialChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    OutlinedTextField(
        value = initial,
        onValueChange = { onInitialChange(it.uppercase()) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = MaterialTheme.shapes.medium,
        label = { Text(labelInitial) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (initial.isNotEmpty()) {
                IconButton(onClick = { onInitialChange("") }) {
                    Icon(
                        imageVector = EvaIcons.Outline.CloseCircle,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@Composable
fun EditNicknameFields(
    nickname: String,
    labelNickname: String = "Nickname",
    onNicknameChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    OutlinedTextField(
        value = nickname,
        onValueChange = { onNicknameChange(it.lowercase()) },
        shape = MaterialTheme.shapes.medium,
        label = { Text(labelNickname) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (nickname.isNotEmpty()) {
                IconButton(onClick = { onNicknameChange("") }) {
                    Icon(
                        imageVector = EvaIcons.Outline.CloseCircle,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun EditBirthdayField(
    selectedDate: String,
    label: String = "Birthday",
    onDateChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    try {
        val parts = selectedDate.split("-")
        if (parts.size == 3) {
            val day = parts[0].toInt()
            val month = parts[1].toInt() - 1
            val year = parts[2].toInt()
            calendar.set(year, month, day)
        }
    } catch (_: Exception) {
    }
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formatted = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            onDateChange(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { datePicker.show() }) {
                Icon(
                    imageVector = EvaIcons.Outline.Calendar,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGenderDropdown(
    selected: String,
    label: String = "Gender",
    onSelect: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var showPicker by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    OutlinedTextField(
        value = selected,
        onValueChange = {},
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                Icon(
                    imageVector = EvaIcons.Outline.ArrowIosDownward,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    )
    if (showPicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showPicker = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Gender",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                listOf(
                    "Male" to "Laki-laki",
                    "Female" to "Perempuan"
                ).forEach { (en, id) ->
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = if (selected == en) 2.dp else 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 2.dp)
                            .clickable {
                                onSelect(en)
                                showPicker = false
                            },
                        color = if (selected == en)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = en,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selected == en)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = id,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { showPicker = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Cancel")
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun EditEmailField(
    label: String = "New Email",
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        singleLine = true,
        label = { Text(label) },
        placeholder = {
            Text(
                text = PLACE_HOLDER_MAIL,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = EvaIcons.Outline.CloseCircle,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@Composable
fun EditPhoneField(
    label: String = "New Phone Number",
    value: String,
    onValueChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.filter { c -> c.isDigit() }) },
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        singleLine = true,
        label = { Text(label) },
        placeholder = {
            Text(
                text = PLACE_HOLDER_PHONE,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = EvaIcons.Outline.CloseCircle,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@Composable
fun EditPasswordFieldWithStrength(
    labelNewPassword: String = "New Password",
    valueNewPassword: String,
    labelConfirmPassword: String = "Confirm Password",
    valueConfirmPassword: String,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    val newStrength by remember(valueNewPassword) {
        derivedStateOf {
            validatePassword(
                valueNewPassword
            )
        }
    }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    val focusNew = remember { FocusRequester() }
    val focusConfirm = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    val allChecklistPassed by remember(valueNewPassword) {
        derivedStateOf { newStrength.score == 5 }
    }

    val passwordsMatch by remember(valueNewPassword, valueConfirmPassword) {
        derivedStateOf {
            valueConfirmPassword.isNotEmpty() && valueNewPassword == valueConfirmPassword
        }
    }

    // Request focus to New on first composition
    LaunchedEffect(Unit) { focusNew.requestFocus() }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        // ---------------------------
        // 🟢 Step 1: NEW PASSWORD
        // ---------------------------
        OutlinedTextField(
            value = valueNewPassword,
            onValueChange = onNewPasswordChange,
            shape = MaterialTheme.shapes.medium,
            label = { Text(labelNewPassword) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusNew),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (newVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (newVisible) Icons.Outlined.Abc else Icons.Outlined.Password
                IconButton(onClick = { newVisible = !newVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        )

        // Strength Bar & Checklist (visible while checklist NOT yet all passed)
        AnimatedVisibility(visible = valueNewPassword.isNotEmpty() && !allChecklistPassed) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                val animatedProgress by animateFloatAsState(
                    targetValue = newStrength.score / 5f,
                    animationSpec = tween(400),
                    label = "strengthAnim"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = newStrength.color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )

                Text(
                    text = "Strength: ${newStrength.label}",
                    color = newStrength.color,
                    style = MaterialTheme.typography.labelMedium
                )

                PasswordRequirementsChecklist(password = valueNewPassword)
            }
        }

        // ---------------------------
        // 🟡 Step 2: CONFIRM PASSWORD (only appear when checklist OK)
        // ---------------------------
        AnimatedVisibility(visible = allChecklistPassed) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = valueConfirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    shape = MaterialTheme.shapes.medium,
                    label = { Text(labelConfirmPassword) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusConfirm),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (confirmVisible) Icons.Outlined.Abc else Icons.Outlined.Password
                        IconButton(onClick = { confirmVisible = !confirmVisible }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                )

                val matchColor by animateColorAsState(
                    if (passwordsMatch) MaterialTheme.colorScheme.onSurface else Color.Gray,
                    label = "matchColor"
                )

                Text(
                    text = when {
                        valueConfirmPassword.isEmpty() -> "Re-enter your new password"
                        passwordsMatch -> "Passwords match"
                        else -> "Passwords do not match"
                    },
                    color = matchColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    LaunchedEffect(allChecklistPassed) {
        if (allChecklistPassed) {
            delay(150)
            focusConfirm.requestFocus()
            keyboardController?.show()
        }
    }
}

@Composable
fun PasswordRequirementsChecklist(password: String) {
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }
    val hasLength = password.length >= 8

    val requirements = listOf(
        hasLength to "At least 8 characters",
        hasUpper to "Contains uppercase letter",
        hasLower to "Contains lowercase letter",
        hasDigit to "Contains number",
        hasSpecial to "Contains special character"
    )

    AnimatedVisibility(
        visible = password.isNotEmpty(),
        enter = slideInVertically(initialOffsetY = { it / 3 }) + fadeIn(),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            requirements.forEach { (fulfilled, text) ->
                val color by animateColorAsState(
                    if (fulfilled) Color(0xFF00C853) else Color.Gray,
                    label = "requirementColor"
                )
                val scale by animateFloatAsState(
                    targetValue = if (fulfilled) 1.05f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "requirementScale"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                ) {
                    val icon = if (fulfilled) "✅" else "❌"
                    Text(
                        text = "$icon  $text",
                        color = color,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedPinField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val maxLength = 4

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(maxLength) { index ->
                val filled = index < value.length
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
                            if (filled) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        AnimatedNumericKeypad(
            onDigit = { digit ->
                if (value.length < maxLength) onValueChange(value + digit)
            },
            onDelete = {
                if (value.isNotEmpty()) onValueChange(value.dropLast(1))
            }
        )
    }
}

@Composable
fun AnimatedNumericKeypad(
    onDigit: (String) -> Unit,
    onDelete: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "⌫")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        keys.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { key ->
                    when (key) {
                        "" -> Spacer(Modifier.size(64.dp))
                        "⌫" -> AnimatedKeyButton(key, onDelete)
                        else -> AnimatedKeyButton(key) { onDigit(key) }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedKeyButton(
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "keyPressScale"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = sourceCodeProFontFamily
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedNumericKeypadPreview() {
    ArkheTheme {
        AnimatedNumericKeypad(onDigit = {}, onDelete = {})
    }
}
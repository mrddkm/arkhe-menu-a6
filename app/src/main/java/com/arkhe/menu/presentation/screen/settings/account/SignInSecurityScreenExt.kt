package com.arkhe.menu.presentation.screen.settings.account

import android.annotation.SuppressLint
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arkhe.menu.domain.model.PasswordData
import com.arkhe.menu.domain.model.PinData
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.ui.components.edit.EditableField
import com.arkhe.menu.presentation.ui.components.edit.EditableInfoScreenBase
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.EyeOff
import kotlinx.coroutines.launch

@Composable
fun SignInSecurityScreenExt(
    onBackClick: () -> Unit,
    navController: NavController? = null,
    passwordData: PasswordData,
    pinData: PinData,
    onPasswordUpdate: (PasswordData) -> Unit,
    onPinUpdate: (PinData) -> Unit
) {
    val handleBackNavigation: () -> Unit = {
        navController?.let { nav ->
            val popSuccess = nav.popBackStack()
            if (!popSuccess) {
                nav.navigate(NavigationRoute.MAIN) {
                    popUpTo(NavigationRoute.MAIN) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        } ?: run {
            onBackClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = handleBackNavigation) {
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
//        ChangePasswordScreen(passwordData, onPasswordUpdate)
//        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        ChangePinScreen(pinData, onPinUpdate)
    }
}

// -----------------------------------------------------------------------------
// 🧱 Change Password Screen (with Snackbar Feedback)
// -----------------------------------------------------------------------------
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    passwordData: PasswordData,
    onPasswordUpdate: (PasswordData) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var lastStrength by remember { mutableStateOf("None") }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        EditableInfoScreenBase(
            title = "Change Password",
            userData = passwordData,
            onUserUpdate = onPasswordUpdate,
            fields = listOf(
                EditableField(
                    label = "New Password",
                    getValue = { it.newPassword },
                    applyChange = { old, new -> old.copy(newPassword = new) },
                    isValid = { validatePassword(it).isStrongEnough },
                    editor = { value, onValueChange ->
                        val strength = validatePassword(value)

                        // Detect strength change
                        if (value.isNotEmpty() && strength.label != lastStrength) {
                            lastStrength = strength.label
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Password Strength: ${strength.label}",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }

                        EditPasswordFieldWithStrength(
                            label = "New Password",
                            value = value,
                            onValueChange = onValueChange
                        )
                    }
                ),
                EditableField(
                    label = "Confirm Password",
                    getValue = { it.confirmPassword },
                    applyChange = { old, new -> old.copy(confirmPassword = new) },
                    isValid = { it == passwordData.newPassword && validatePassword(it).isStrongEnough },
                    editor = { value, onValueChange ->
                        EditPasswordField(
                            label = "Confirm Password",
                            value = value,
                            onValueChange = onValueChange
                        )
                    }
                )
            ),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// -----------------------------------------------------------------------------
// 🧠 Password Strength Validation
// -----------------------------------------------------------------------------
data class PasswordStrength(
    val label: String,
    val color: Color,
    val score: Int,
    val isStrongEnough: Boolean
)

fun validatePassword(password: String): PasswordStrength {
    val hasLength = password.length >= 8
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }

    val score = listOf(hasLength, hasUpper, hasLower, hasDigit, hasSpecial).count { it }

    return when (score) {
        in 0..2 -> PasswordStrength("Weak", Color.Red, score, false)
        3, 4 -> PasswordStrength("Medium", Color(0xFFFFC107), score, false)
        5 -> PasswordStrength("Strong", Color(0xFF00C853), score, true)
        else -> PasswordStrength("Weak", Color.Red, score, false)
    }
}

// -----------------------------------------------------------------------------
// 🔡 Password Editors + Strength + Checklist
// -----------------------------------------------------------------------------
@Composable
fun EditPasswordFieldWithStrength(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val strength by remember(value) { derivedStateOf { validatePassword(value) } }
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (isVisible) EvaIcons.Outline.EyeOff else EvaIcons.Outline.Eye
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(icon, contentDescription = "Toggle visibility")
                }
            }
        )

        AnimatedVisibility(visible = value.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                val animatedProgress by animateFloatAsState(
                    targetValue = strength.score / 5f,
                    animationSpec = tween(durationMillis = 400),
                    label = "strengthProgress"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = strength.color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )

                Text(
                    text = "Strength: ${strength.label}",
                    color = strength.color,
                    style = MaterialTheme.typography.labelMedium
                )

                PasswordRequirementsChecklist(password = value)
            }
        }
    }
}

@Composable
fun EditPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (isVisible) EvaIcons.Outline.EyeOff else EvaIcons.Outline.Eye
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(icon, contentDescription = "Toggle visibility")
            }
        }
    )
}

// -----------------------------------------------------------------------------
// ✅ Animated Checklist
// -----------------------------------------------------------------------------
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

// -----------------------------------------------------------------------------
// 🔢 Change PIN Screen
// -----------------------------------------------------------------------------
@Composable
fun ChangePinScreen(
    pinData: PinData,
    onPinUpdate: (PinData) -> Unit
) {
    EditableInfoScreenBase(
        title = "Change PIN",
        userData = pinData,
        onUserUpdate = onPinUpdate,
        fields = listOf(
            EditableField(
                label = "New PIN",
                getValue = { it.newPin },
                applyChange = { old, new -> old.copy(newPin = new) },
                isValid = { it.length == 4 },
                editor = { value, onValueChange ->
                    AnimatedPinField(
                        label = "Enter New PIN",
                        value = value,
                        onValueChange = onValueChange
                    )
                }
            ),
            EditableField(
                label = "Confirm PIN",
                getValue = { it.confirmPin },
                applyChange = { old, new -> old.copy(confirmPin = new) },
                isValid = { it == pinData.newPin && it.length == 4 },
                editor = { value, onValueChange ->
                    AnimatedPinField(
                        label = "Confirm PIN",
                        value = value,
                        onValueChange = onValueChange
                    )
                }
            )
        )
    )
}

// -----------------------------------------------------------------------------
// 🔢 Animated PIN Input + Keypad
// -----------------------------------------------------------------------------
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
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
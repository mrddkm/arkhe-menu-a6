package com.arkhe.menu.presentation.ui.components.edit

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.LoadingGraySpinner
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Edit2
import compose.icons.evaicons.outline.Save
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Base template untuk screen edit data per-field
 *
 * Gunakan ini untuk membuat halaman seperti Personal Info, Contact Info, dsb.
 *
 * Tugasnya:
 * - Render daftar field editable (list)
 * - Tampilkan bottom sheet edit per field
 * - Menangani validasi, save, loading, toast, dan state yang efisien
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> EditableInfoScreenBase(
    title: String,
    userData: T,
    fields: List<EditableField<T>>,
    onUserUpdate: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingField by remember { mutableStateOf<EditableField<T>?>(null) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        /*Render each field efficiently*/
        fields.forEach { field ->
            EditableFieldRow(
                label = field.label,
                valueLabel = field.valueLabel,
                value = field.getValue(userData),
                info = field.info,
                showIcon = field.showIcon,
                showDivider = field.showDivider,
                onEditClick = { editingField = field }
            )
        }
    }

    /*--- Bottom sheet (appears only when the user clicks edit) ---*/
    if (editingField != null) {
        val field = editingField!!
        ModalBottomSheet(
            onDismissRequest = { },
            sheetState = sheetState,
            dragHandle = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { editingField = null }
                    ) {
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
                    Box(
                        modifier = Modifier
                            .height(14.dp)
                            .width(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Fill.Lock,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(Modifier.width(48.dp))
                }
            }
        ) {
            var value by remember { mutableStateOf(field.getValue(userData)) }
            var confirmValue by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            val isChanged by remember { derivedStateOf { value != field.getValue(userData) } }
            val isValid by remember { derivedStateOf { field.isValid(value) } }

            val combinedIsValid by remember(value, confirmValue) {
                derivedStateOf {
                    when (field.label?.lowercase()) {
                        "password", "new password" -> {
                            val strength = validatePassword(value)
                            strength.score == 5 && confirmValue.isNotEmpty() && value == confirmValue
                        }

                        else -> isValid
                    }
                }
            }

            /*✅ Password-specific derived states*/
            val isPasswordField = field.label?.contains("password", ignoreCase = true) == true
            val strength by remember(value) { derivedStateOf { validatePassword(value) } }
            val passwordsMatch by remember(value, confirmValue) {
                derivedStateOf { confirmValue.isNotEmpty() && value == confirmValue }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                )

                /*Field editor UI*/
                if (isPasswordField) {
                    EditPasswordFieldWithStrength(
                        labelNewPassword = "New Password",
                        valueNewPassword = value,
                        labelConfirmPassword = "Confirm Password",
                        valueConfirmPassword = confirmValue,
                        onNewPasswordChange = { value = it },
                        onConfirmPasswordChange = { confirmValue = it }
                    )
                } else {
                    field.editor(value) { value = it }
                }

                /*Action buttons*/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { editingField = null },
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .width(65.dp)
                                .height(25.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Cancel")
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                delay(500) /*API simulation*/
                                val newUser = field.applyChange(userData, value)
                                onUserUpdate(newUser)
                                Toast
                                    .makeText(context, "Saved successfully", Toast.LENGTH_SHORT)
                                    .show()
                                isLoading = false
                                editingField = null
                            }
                        },
                        enabled = isChanged && isValid && combinedIsValid && !isLoading
                    ) {
                        if (isLoading)
                            Row(
                                modifier = Modifier
                                    .width(65.dp)
                                    .height(25.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                LoadingGraySpinner(modifier = Modifier.fillMaxSize())
                            }
                        else
                            Row(
                                modifier = Modifier
                                    .width(65.dp)
                                    .height(25.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    imageVector = EvaIcons.Outline.Save,
                                    contentDescription = null
                                )
                                Text("Save")
                            }
                    }
                }

                // ------------------------------
                // 🟢 Micro-feedback Password only
                // ------------------------------
                AnimatedVisibility(visible = isPasswordField) {
                    val feedbackText: String
                    val feedbackColor: Color

                    when {
                        strength.score < 5 -> {
                            feedbackText = "⚠️ Password too weak"
                            feedbackColor = Color(0xFFFFA000)
                        }

                        !passwordsMatch -> {
                            feedbackText = "🔒 Passwords don’t match"
                            feedbackColor = Color(0xFFD50000)
                        }

                        else -> {
                            feedbackText = "✅ Ready to save"
                            feedbackColor = Color(0xFF00C853)
                        }
                    }

                    val animatedColor by animateColorAsState(
                        targetValue = feedbackColor,
                        animationSpec = tween(300),
                        label = "feedbackColorAnim"
                    )

                    Text(
                        text = feedbackText,
                        color = animatedColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.End)
                    )
                }
            }
        }
    }
}

/**
 * Model deklaratif untuk tiap field editable
 */
data class EditableField<T>(
    val label: String? = null,
    val valueLabel: String? = null,
    val getValue: (T) -> String,
    val info: String? = null,
    val showIcon: Boolean = true,
    val showDivider: Boolean = true,
    val applyChange: (T, String) -> T,
    val isValid: (String) -> Boolean = { it.isNotBlank() },
    val editor: @Composable (value: String, onValueChange: (String) -> Unit) -> Unit
)

/**
 * Baris item untuk field editable
 */
@Composable
private fun EditableFieldRow(
    label: String? = null,
    valueLabel: String? = null,
    value: String? = null,
    info: String? = null,
    showIcon: Boolean = true,
    showDivider: Boolean = true,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onEditClick() }
            .padding(start = 20.dp, top = 12.dp, bottom = 0.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                label?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
                if (valueLabel != null) {
                    Text(
                        text = valueLabel,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    value?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                info?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            if (showIcon) {
                Icon(
                    imageVector = EvaIcons.Outline.Edit2,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
    if (showDivider) {
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 20.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )
    } else Spacer(modifier = Modifier.height(4.dp))
}

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

data class PasswordStrength(
    val label: String,
    val color: Color,
    val score: Int,
    val isStrongEnough: Boolean
)

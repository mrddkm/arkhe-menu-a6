package com.arkhe.menu.presentation.ui.components.edit

import android.widget.Toast
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
        /*Render setiap field secara efisien*/
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

    /*--- Bottom sheet (muncul hanya ketika user klik edit) ---*/
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
            val isChanged by remember { derivedStateOf { value != field.getValue(userData) } }
            val isValid by remember { derivedStateOf { field.isValid(value) } }
            var isLoading by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }

                /*Field editor UI*/
                field.editor(
                    value
                ) { value = it }

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
                        enabled = isChanged && isValid && !isLoading
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
    /*    Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = EvaIcons.Outline.Edit2,
                    contentDescription = "Edit $label",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }*/
}

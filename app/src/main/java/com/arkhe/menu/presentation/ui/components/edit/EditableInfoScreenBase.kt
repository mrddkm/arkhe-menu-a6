package com.arkhe.menu.presentation.ui.components.edit

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.LoadingGraySpinner
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Edit2
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
        skipPartiallyExpanded = true
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Render setiap field secara efisien
        fields.forEach { field ->
            EditableFieldRow(
                label = field.label,
                value = field.getValue(userData),
                onEditClick = { editingField = field }
            )
        }
    }

    // --- Bottom sheet (muncul hanya ketika user klik edit)
    if (editingField != null) {
        val field = editingField!!

        ModalBottomSheet(
            onDismissRequest = { /* tidak bisa tutup lewat luar */ },
            sheetState = sheetState
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
                Text(
                    text = "Edit ${field.label}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Field editor UI
                field.editor(
                    value
                ) { value = it }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { editingField = null },
                        enabled = !isLoading
                    ) { Text("Cancel") }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                delay(500) // simulasi API
                                val newUser = field.applyChange(userData, value)
                                onUserUpdate(newUser)
                                Toast
                                    .makeText(context, "Berhasil disimpan", Toast.LENGTH_SHORT)
                                    .show()
                                isLoading = false
                                editingField = null
                            }
                        },
                        enabled = isChanged && isValid && !isLoading
                    ) {
                        if (isLoading)
                            LoadingGraySpinner(modifier = Modifier.size(24.dp))
                        else
                            Text("Save")
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
    val label: String,
    val getValue: (T) -> String,
    val applyChange: (T, String) -> T,
    val isValid: (String) -> Boolean = { it.isNotBlank() },
    val editor: @Composable (value: String, onValueChange: (String) -> Unit) -> Unit
)

/**
 * Baris item untuk field editable
 */
@Composable
private fun EditableFieldRow(
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    Row(
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
    }
}

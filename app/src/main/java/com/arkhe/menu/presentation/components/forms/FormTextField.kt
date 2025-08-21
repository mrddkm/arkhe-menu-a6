package com.arkhe.menu.presentation.components.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    minLines: Int = 1,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(if (isRequired) "$label *" else label)
        },
        modifier = modifier.fillMaxWidth(),
        minLines = minLines,
        isError = isRequired && value.isBlank()
    )
}
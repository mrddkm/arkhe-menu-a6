package com.arkhe.menu.presentation.ui.components.edit

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.LoadingGraySpinner
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Save
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditBottomSheetBase(
    title: String,
    isChanged: Boolean,
    onCancel: () -> Unit,
    onSave: suspend () -> Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

        content()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onCancel, enabled = !isLoading,
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
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(500) /*API simulation*/
                        val success = onSave()
                        Toast.makeText(
                            context,
                            if (success) "Saved successfully" else "Failed to save",
                            Toast.LENGTH_SHORT
                        ).show()
                        isLoading = false
                        if (success) onCancel()
                    }
                },
                enabled = isChanged && !isLoading,
                shape = MaterialTheme.shapes.medium
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

@Preview(showBackground = true)
@Composable
fun EditBottomSheetBasePreview() {
    ArkheTheme {
        EditBottomSheetBase(
            title = "Changes to your Name will be reflected across your Account.",
            isChanged = true,
            onCancel = { false },
            onSave = {
                true
            }
        ) {
            EditNameField(
                value = "",
                label = "",
                onValueChange = { it },
                onClear = { "" }
            )
        }
    }
}

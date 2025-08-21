package com.arkhe.menu.presentation.components.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.PaymentMethod
import com.arkhe.menu.domain.model.ReceiptItem
import com.arkhe.menu.presentation.utils.formatCurrency
import com.arkhe.menu.presentation.viewmodel.receipt.CreateReceiptUiState

@Composable
fun ReceiptForm(
    uiState: CreateReceiptUiState,
    onCustomerNameChange: (String) -> Unit,
    onCustomerEmailChange: (String) -> Unit,
    onCustomerPhoneChange: (String) -> Unit,
    onPaymentMethodChange: (PaymentMethod) -> Unit,
    onNotesChange: (String) -> Unit,
    onAddItem: (ReceiptItem) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onUpdateItem: (Int, ReceiptItem) -> Unit
) {
    var showAddItemDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Customer Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Customer Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                FormTextField(
                    value = uiState.customerName,
                    onValueChange = onCustomerNameChange,
                    label = "Customer Name",
                    isRequired = true
                )

                FormTextField(
                    value = uiState.customerEmail,
                    onValueChange = onCustomerEmailChange,
                    label = "Email (Optional)"
                )

                FormTextField(
                    value = uiState.customerPhone,
                    onValueChange = onCustomerPhoneChange,
                    label = "Phone (Optional)"
                )
            }
        }

        // Items Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { showAddItemDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Item")
                    }
                }

                if (uiState.items.isNotEmpty()) {
                    uiState.items.forEachIndexed { index, item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${item.quantity} x ${formatCurrency(item.unitPrice)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = formatCurrency(item.totalPrice),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(onClick = { onRemoveItem(index) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove Item",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No items added",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        // Payment Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Payment Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                FormDropdown(
                    value = uiState.paymentMethod,
                    onValueChange = onPaymentMethodChange,
                    label = "Payment Method",
                    options = PaymentMethod.values().toList(),
                    optionText = { it.name.replace("_", " ") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calculations
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal:")
                        Text(formatCurrency(uiState.subtotal))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tax (10%):")
                        Text(formatCurrency(uiState.tax))
                    }

                    if (uiState.discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Discount:")
                            Text(
                                "-${formatCurrency(uiState.discount)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Divider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            formatCurrency(uiState.total),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Notes
        FormTextField(
            value = uiState.notes,
            onValueChange = onNotesChange,
            label = "Notes (Optional)",
            minLines = 3
        )
    }

    // Add Item Dialog
    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onAddItem = { item ->
                onAddItem(item)
                showAddItemDialog = false
            }
        )
    }
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (ReceiptItem) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unitPrice by remember { mutableStateOf("") }

    val totalPrice = try {
        val qty = quantity.toIntOrNull() ?: 0
        val price = unitPrice.toDoubleOrNull() ?: 0.0
        qty * price
    } catch (e: Exception) {
        0.0
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Item") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = itemDescription,
                    onValueChange = { itemDescription = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = unitPrice,
                        onValueChange = { unitPrice = it },
                        label = { Text("Unit Price") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = "Total: ${formatCurrency(totalPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (itemName.isNotBlank() && quantity.isNotBlank() && unitPrice.isNotBlank()) {
                        val item = ReceiptItem(
                            name = itemName,
                            description = itemDescription.takeIf { it.isNotBlank() },
                            quantity = quantity.toIntOrNull() ?: 1,
                            unitPrice = unitPrice.toDoubleOrNull() ?: 0.0,
                            totalPrice = totalPrice
                        )
                        onAddItem(item)
                    }
                },
                enabled = itemName.isNotBlank() && quantity.isNotBlank() && unitPrice.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
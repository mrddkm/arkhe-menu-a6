package com.arkhe.menu.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.arkhe.menu.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*Date formatting*/
@Suppress("UNUSED")
fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(date)
}

/*Currency formatting*/
@Suppress("UNUSED", "DEPRECATION")
fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount)
}

/*Single or Multiple*/
@Composable
fun formatItemCount(count: Int): String {
    return if (count > 1) {
        "$count ${stringResource(R.string.products)}"
    } else {
        "$count ${stringResource(R.string.product)}"
    }
}
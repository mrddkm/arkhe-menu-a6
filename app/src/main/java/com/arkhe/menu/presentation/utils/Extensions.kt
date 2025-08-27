@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.utils

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
@Suppress("DEPRECATION")
fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount)
}
package com.arkhe.menu.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    @SuppressLint("ConstantLocale")
    private val dateTimeFormat = SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault())
    @SuppressLint("ConstantLocale")
    private val isoDateFormat = SimpleDateFormat(Constants.ISO_DATE_FORMAT, Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun formatDateTime(date: Date): String {
        return dateTimeFormat.format(date)
    }

    fun formatToISO(date: Date): String {
        return isoDateFormat.format(date)
    }

    fun parseISO(dateString: String): Date? {
        return try {
            isoDateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val checkDate = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isThisWeek(date: Date): Boolean {
        val today = Calendar.getInstance()
        val checkDate = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                today.get(Calendar.WEEK_OF_YEAR) == checkDate.get(Calendar.WEEK_OF_YEAR)
    }

    fun isThisMonth(date: Date): Boolean {
        val today = Calendar.getInstance()
        val checkDate = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == checkDate.get(Calendar.MONTH)
    }

    fun getDaysFromNow(date: Date): Long {
        val now = System.currentTimeMillis()
        val targetTime = date.time
        return (targetTime - now) / (24 * 60 * 60 * 1000)
    }

    fun getRelativeTimeString(date: Date): String {
        val now = Date()
        val diff = now.time - date.time

        return when {
            diff < 60 * 1000 -> "Just now"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} minutes ago"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} hours ago"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)} days ago"
            else -> formatDate(date)
        }
    }
}
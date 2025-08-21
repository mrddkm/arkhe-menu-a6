package com.arkhe.menu.data.local.converter

import androidx.room.TypeConverter
import com.arkhe.menu.domain.model.*
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTripCategory(category: TripCategory): String {
        return category.name
    }

    @TypeConverter
    fun toTripCategory(category: String): TripCategory {
        return TripCategory.valueOf(category)
    }

    @TypeConverter
    fun fromTripStatus(status: TripStatus): String {
        return status.name
    }

    @TypeConverter
    fun toTripStatus(status: String): TripStatus {
        return TripStatus.valueOf(status)
    }

    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String {
        return method.name
    }

    @TypeConverter
    fun toPaymentMethod(method: String): PaymentMethod {
        return PaymentMethod.valueOf(method)
    }

    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String {
        return status.name
    }

    @TypeConverter
    fun toPaymentStatus(status: String): PaymentStatus {
        return PaymentStatus.valueOf(status)
    }
}
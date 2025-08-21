package com.arkhe.menu.data.local.converter

import androidx.room.TypeConverter
import com.arkhe.menu.domain.model.PaymentMethod
import com.arkhe.menu.domain.model.PaymentStatus
import com.arkhe.menu.domain.model.TripCategory
import com.arkhe.menu.domain.model.TripStatus

class EnumConverter {
    // TripCategory
    @TypeConverter
    fun fromTripCategory(category: TripCategory): String = category.name

    @TypeConverter
    fun toTripCategory(category: String): TripCategory = TripCategory.valueOf(category)

    // TripStatus
    @TypeConverter
    fun fromTripStatus(status: TripStatus): String = status.name

    @TypeConverter
    fun toTripStatus(status: String): TripStatus = TripStatus.valueOf(status)

    // PaymentMethod
    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String = method.name

    @TypeConverter
    fun toPaymentMethod(method: String): PaymentMethod = PaymentMethod.valueOf(method)

    // PaymentStatus
    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String = status.name

    @TypeConverter
    fun toPaymentStatus(status: String): PaymentStatus = PaymentStatus.valueOf(status)
}
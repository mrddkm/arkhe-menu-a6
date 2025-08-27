@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.domain.model

enum class UserRole(val displayName: String, val code: String) {
    SUPERUSER("Administrator", "superuser"),
    FAGA("Finance, Accounting and General Affair", "faga"),
    MCC("Marketing & Content Creator", "mcc"),
    BROD("Business, Research, Operational & Development", "brod"),
    PRESDIR("President Director", "presdir"),
    EMPLOYEE("Employee", "employee");

    companion object {
        fun fromCode(code: String): UserRole {
            return entries.find { it.code == code } ?: EMPLOYEE
        }
    }
}
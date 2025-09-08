package com.arkhe.menu.utils

// 1. Data Class untuk Phone Number
data class PhoneNumber(
    val original: String,
    val cleaned: String,
    val formatted: String,
    val international: String,
    val countryCode: String,
    val nationalNumber: String,
    val isValid: Boolean,
    val provider: String? = null,
    val type: PhoneType = PhoneType.UNKNOWN
)

enum class PhoneType {
    MOBILE, LANDLINE, UNKNOWN
}

// 2. Phone Number Parser Class
class PhoneNumberParser {

    companion object {
        // Operator seluler Indonesia
        private val MOBILE_PREFIXES = mapOf(
            "0811" to "Telkomsel (Halo)",
            "0812" to "Telkomsel (simPATI)",
            "0813" to "Telkomsel (simPATI)",
            "0821" to "Telkomsel (Kartu As)",
            "0822" to "Telkomsel (Kartu As)",
            "0823" to "Telkomsel (Kartu As)",
            "0851" to "Telkomsel (Kartu As)",
            "0852" to "Telkomsel (Kartu As)",
            "0853" to "Telkomsel (Kartu As)",

            "0814" to "Indosat (IM3)",
            "0815" to "Indosat (Matrix)",
            "0816" to "Indosat (Matrix)",
            "0855" to "Indosat (IM3)",
            "0856" to "Indosat (IM3)",
            "0857" to "Indosat (IM3)",
            "0858" to "Indosat (Mentari)",

            "0817" to "XL",
            "0818" to "XL",
            "0819" to "XL",
            "0859" to "XL",
            "0877" to "XL",
            "0878" to "XL",

            "0838" to "Axis",
            "0831" to "Axis",
            "0832" to "Axis",
            "0833" to "Axis",

            "0895" to "Three (3)",
            "0896" to "Three (3)",
            "0897" to "Three (3)",
            "0898" to "Three (3)",
            "0899" to "Three (3)",

            "0881" to "Smartfren",
            "0882" to "Smartfren",
            "0883" to "Smartfren",
            "0884" to "Smartfren",
            "0885" to "Smartfren",
            "0886" to "Smartfren",
            "0887" to "Smartfren",
            "0888" to "Smartfren",
            "0889" to "Smartfren"
        )

        private val LANDLINE_AREA_CODES = mapOf(
            "021" to "Jakarta",
            "022" to "Bandung",
            "024" to "Semarang",
            "031" to "Surabaya",
            "061" to "Medan",
            "0274" to "Yogyakarta",
            "0341" to "Malang",
            "0361" to "Denpasar"
            // Add more area codes as needed
        )
    }

    // Bersihkan nomor dari karakter non-digit
    private fun cleanPhoneNumber(input: String): String {
        return input.replace(Regex("[^0-9+]"), "")
    }

    // Normalisasi nomor ke format standar Indonesia
    private fun normalizeIndonesianNumber(cleaned: String): String {
        return when {
            // Jika dimulai dengan +62
            cleaned.startsWith("+62") -> "0" + cleaned.substring(3)
            // Jika dimulai dengan 62 tanpa +
            cleaned.startsWith("62") && cleaned.length > 10 -> "0" + cleaned.substring(2)
            // Jika dimulai dengan 8 (format internasional tanpa 62)
            cleaned.startsWith("8") && cleaned.length >= 10 -> "0$cleaned"
            // Jika sudah benar dimulai dengan 0
            cleaned.startsWith("0") -> cleaned
            else -> cleaned
        }
    }

    // Deteksi provider berdasarkan prefix
    private fun detectProvider(normalized: String): String? {
        // Untuk mobile (4 digit pertama)
        val mobilePrefix = normalized.take(4)
        MOBILE_PREFIXES[mobilePrefix]?.let { return it }

        // Untuk landline (3 digit pertama)
        val landlinePrefix = normalized.take(3)
        LANDLINE_AREA_CODES[landlinePrefix]?.let { return "Landline - $it" }

        // Untuk landline (4 digit pertama seperti Yogyakarta)
        val landlinePrefix4 = normalized.take(4)
        LANDLINE_AREA_CODES[landlinePrefix4]?.let { return "Landline - $it" }

        return null
    }

    // Deteksi tipe nomor
    private fun detectPhoneType(normalized: String): PhoneType {
        // Mobile number (starts with 08)
        if (normalized.startsWith("08") && normalized.length >= 10) {
            val prefix = normalized.take(4)
            if (MOBILE_PREFIXES.containsKey(prefix)) {
                return PhoneType.MOBILE
            }
        }

        // Landline (starts with 0 but not 08)
        if (normalized.startsWith("0") && !normalized.startsWith("08")) {
            return PhoneType.LANDLINE
        }

        return PhoneType.UNKNOWN
    }

    // Validasi nomor telepon Indonesia
    private fun isValidIndonesianNumber(normalized: String): Boolean {
        return when {
            // Mobile: 08xx-xxxx-xxxx (10-13 digits)
            normalized.startsWith("08") -> {
                normalized.length in 10..13 &&
                        MOBILE_PREFIXES.keys.any { normalized.startsWith(it) }
            }
            // Landline: 0xx-xxxx-xxxx (9-12 digits)
            normalized.startsWith("0") && !normalized.startsWith("08") -> {
                normalized.length in 9..12
            }
            else -> false
        }
    }

    // Format nomor telepon untuk display
    private fun formatPhoneNumber(normalized: String, type: PhoneType): String {
        return when (type) {
            PhoneType.MOBILE -> {
                when (normalized.length) {
                    10 -> "${normalized.substring(0, 4)}-${normalized.substring(4, 6)}-${normalized.substring(6)}"
                    11 -> "${normalized.substring(0, 4)}-${normalized.substring(4, 7)}-${normalized.substring(7)}"
                    12 -> "${normalized.substring(0, 4)}-${normalized.substring(4, 8)}-${normalized.substring(8)}"
                    13 -> "${normalized.substring(0, 4)}-${normalized.substring(4, 8)}-${normalized.substring(8)}"
                    else -> normalized
                }
            }
            PhoneType.LANDLINE -> {
                when {
                    normalized.length == 9 -> "${normalized.substring(0, 3)}-${normalized.substring(3, 6)}-${normalized.substring(6)}"
                    normalized.length == 10 -> "${normalized.substring(0, 3)}-${normalized.substring(3, 7)}-${normalized.substring(7)}"
                    normalized.length >= 11 -> "${normalized.substring(0, 4)}-${normalized.substring(4, 8)}-${normalized.substring(8)}"
                    else -> normalized
                }
            }
            else -> normalized
        }
    }

    // Format ke international (+62)
    private fun toInternationalFormat(normalized: String): String {
        if (normalized.startsWith("0") && normalized.length > 1) {
            return "+62${normalized.substring(1)}"
        }
        return normalized
    }

    // Main parsing function
    fun parsePhoneNumber(input: String): PhoneNumber {
        val cleaned = cleanPhoneNumber(input)
        val normalized = normalizeIndonesianNumber(cleaned)
        val type = detectPhoneType(normalized)
        val isValid = isValidIndonesianNumber(normalized)
        val formatted = if (isValid) formatPhoneNumber(normalized, type) else normalized
        val international = if (isValid) toInternationalFormat(normalized) else normalized
        val provider = if (isValid) detectProvider(normalized) else null
        val nationalNumber = if (normalized.startsWith("0")) normalized.substring(1) else normalized

        return PhoneNumber(
            original = input,
            cleaned = cleaned,
            formatted = formatted,
            international = international,
            countryCode = "+62",
            nationalNumber = nationalNumber,
            isValid = isValid,
            provider = provider,
            type = type
        )
    }

    // Batch parsing untuk multiple numbers
    fun parsePhoneNumbers(inputs: List<String>): List<PhoneNumber> {
        return inputs.map { parsePhoneNumber(it) }
    }

    // Validasi sederhana
    fun isValidPhoneNumber(input: String): Boolean {
        return parsePhoneNumber(input).isValid
    }
}

fun formatToInternationalWithDash(phoneNumber: String): String {
    val parser = PhoneNumberParser()
    return parser.parsePhoneNumber(phoneNumber).international
}

// 3. Extension Functions untuk kemudahan penggunaan
fun String.parseAsPhoneNumber(): PhoneNumber {
    return PhoneNumberParser().parsePhoneNumber(this)
}

fun String.isValidIndonesianPhone(): Boolean {
    return PhoneNumberParser().isValidPhoneNumber(this)
}

// 5. Utility class untuk validasi form
class PhoneNumberValidator {
    companion object {
        fun validateAndFormat(input: String): Pair<Boolean, String> {
            val parser = PhoneNumberParser()
            val result = parser.parsePhoneNumber(input)
            return Pair(result.isValid, result.formatted)
        }

        fun getErrorMessage(input: String): String? {
            val parser = PhoneNumberParser()
            val result = parser.parsePhoneNumber(input)

            return when {
                input.isBlank() -> "Nomor telepon tidak boleh kosong"
                result.cleaned.length < 9 -> "Nomor telepon terlalu pendek"
                result.cleaned.length > 13 -> "Nomor telepon terlalu panjang"
                !result.isValid -> "Format nomor telepon tidak valid"
                else -> null
            }
        }
    }
}
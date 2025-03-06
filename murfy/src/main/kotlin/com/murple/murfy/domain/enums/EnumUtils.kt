package com.murple.murfy.domain.enums


object EnumUtils {
    inline fun <reified T : Enum<T>> fromString(value: String): T {
        return try {
            java.lang.Enum.valueOf(T::class.java, value.uppercase())
        } catch (e: IllegalArgumentException) {
            val values = T::class.java.enumConstants.joinToString()
            throw IllegalArgumentException("Unknown value: $value. Allowed values: $values")
        }
    }
}


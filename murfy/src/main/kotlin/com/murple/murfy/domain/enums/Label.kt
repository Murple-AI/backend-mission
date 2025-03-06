package com.murple.murfy.domain.enums


enum class Label {
    HOME, WORK, OTHER;


    companion object {
        fun fromString(value: String): Label {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Unknown label: $value. Allowed values: ${values().joinToString()}")
            }
        }
    }

}


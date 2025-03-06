package com.murple.murfy.infrastructure.user.jpa.entiy

import com.murple.murfy.domain.user.enums.Gender
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


@Converter(autoApply = true)
class GenderConverter : AttributeConverter<Gender, String> {
    override fun convertToDatabaseColumn(attribute: Gender?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): Gender? {
        return dbData?.let { Gender.valueOf(it) }
    }
}


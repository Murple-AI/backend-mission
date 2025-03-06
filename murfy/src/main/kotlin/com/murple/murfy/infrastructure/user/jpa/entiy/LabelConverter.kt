package com.murple.murfy.infrastructure.user.jpa.entiy


import com.murple.murfy.domain.user.enums.Label
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class LabelConverter : AttributeConverter<Label, String> {
    override fun convertToDatabaseColumn(attribute: Label?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): Label? {
        return dbData?.let { Label.valueOf(it) }
    }
}

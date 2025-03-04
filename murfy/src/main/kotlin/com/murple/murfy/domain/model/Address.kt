package com.murple.murfy.domain.model

class Address(
    val id: Long? = null,
    val label: Label,
    val address: String
) {
    init {
        require(address.isNotBlank() && address.length <= 1024) {
            "주소는 비어있지 않아야 하며 최대 1024자여야 합니다"
        }
    }
}

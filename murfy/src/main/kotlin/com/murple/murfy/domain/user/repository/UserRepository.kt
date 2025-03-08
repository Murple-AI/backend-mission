package com.murple.murfy.domain.user.repository

import com.murple.murfy.domain.user.model.Address
import com.murple.murfy.domain.user.model.Phone
import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.model.UserBasic


interface UserRepository {
    fun save(user: UserAggregate): UserAggregate

    fun findById(id: Long): UserAggregate?

    fun findUserBasicById(id: Long): UserBasic?

    fun delete(id: Long)

    fun findTop5ByNameOrderByCreatedAtAsc(name: String): List<UserBasic>

    fun findByNamesLimitedByCreatedAt(names: List<String>): List<UserBasic>

    fun updateUserBasicInfo(updatedUserBasic: UserBasic): UserBasic

    fun updateUserPhone(userId: Long, updatedPhone: Phone): Phone

    fun updateUserAddress(userId: Long, updatedAddress: Address): Address

    fun addNewPhone(userId: Long, newPhone: Phone): Phone

    fun addNewAddress(userId: Long, newAddress: Address): Address

    fun deleteUserAddress(userId: Long, addressId: Long)

    fun deleteUserPhone(userId: Long, phoneId: Long)
}

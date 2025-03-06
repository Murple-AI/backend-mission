package com.murple.murfy.presentation.user

import com.murple.murfy.application.user.service.UserService
import com.murple.murfy.presentation.user.dto.request.AddressRequest
import com.murple.murfy.presentation.user.dto.request.PhoneRequest
import com.murple.murfy.presentation.user.dto.request.UserBasicInfoRequest
import com.murple.murfy.presentation.user.dto.request.UserRequest
import com.murple.murfy.presentation.user.dto.response.AddressResponse
import com.murple.murfy.presentation.user.dto.response.PhoneResponse
import com.murple.murfy.presentation.user.dto.response.UserBasicInfoResponse
import com.murple.murfy.presentation.user.dto.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    // 사용자 ------------------
    @PostMapping
    fun createUser(@RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        val createdUser = userService.createUser(request.toServiceDto())
        val userResponse = UserResponse.from(createdUser)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.getUserById(id)
        val userResponse = UserResponse.from(user)
        return ResponseEntity.ok(userResponse)
    }


    @PutMapping("/{id}/info")
    fun updateUserInfo(
        @PathVariable id: Long,
        @RequestBody request: UserBasicInfoRequest
    ): ResponseEntity<UserBasicInfoResponse> {
        val updatedUser = userService.updateUserInfo(id, request.toServiceDto())
        val userResponse = UserBasicInfoResponse.from(updatedUser)
        return ResponseEntity.ok(userResponse)
    }

    @DeleteMapping("/{id}")
    fun deleteAllUserInfo(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }


    // phone -----------------------------
    @PostMapping("/{id}/phones")
    fun addUserPhone(
        @PathVariable id: Long,
        @RequestBody request: PhoneRequest
    ): ResponseEntity<PhoneResponse> {
        val createdPhone = userService.addUserPhone(id, request.toServiceDto())
        val phoneResponse = PhoneResponse.from(createdPhone)
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneResponse)
    }

    @DeleteMapping("/{id}/phones/{phoneId}")
    fun deleteUserPhone(
        @PathVariable id: Long,
        @PathVariable phoneId: Long
    ): ResponseEntity<Void> {
        userService.deleteUserPhone(id, phoneId)
        return ResponseEntity.noContent().build()
    }


    @PutMapping("/{id}/phones/{phoneId}")
    fun updateUserPhone(
        @PathVariable id: Long,
        @PathVariable phoneId: Long,
        @RequestBody request: PhoneRequest
    ): ResponseEntity<PhoneResponse> {
        val updatedPhone = userService.updateUserPhone(id, phoneId, request.toServiceDto())
        val phoneResponse = PhoneResponse.from(updatedPhone)
        return ResponseEntity.ok(phoneResponse)
    }

    // address ------------------------
    @PostMapping("/{id}/addresses")
    fun addUserAddress(
        @PathVariable id: Long,
        @RequestBody request: AddressRequest
    ): ResponseEntity<AddressResponse> {
        val createdAddress = userService.addUserAddress(id, request.toServiceDto())
        val addressResponse = AddressResponse.from(createdAddress)
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponse)
    }


    @DeleteMapping("/{id}/addresses/{addressId}")
    fun deleteUserAddress(
        @PathVariable id: Long,
        @PathVariable addressId: Long
    ): ResponseEntity<Void> {
        userService.deleteUserAddress(id, addressId)
        return ResponseEntity.noContent().build()
    }


    @PutMapping("/{id}/addresses/{addressId}")
    fun updateUserAddress(
        @PathVariable id: Long,
        @PathVariable addressId: Long,
        @RequestBody request: AddressRequest
    ): ResponseEntity<AddressResponse> {
        val updatedAddress = userService.updateUserAddress(id, addressId, request.toServiceDto())
        val addressResponse = AddressResponse.from(updatedAddress)
        return ResponseEntity.ok(addressResponse)
    }

}

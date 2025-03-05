package com.murple.murfy.presentation

import com.murple.murfy.application.service.UserService
import com.murple.murfy.presentation.dto.request.UserRequest
import com.murple.murfy.presentation.dto.response.UserDeleteResponse
import com.murple.murfy.presentation.dto.response.UserResponse
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


    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UserRequest
    ): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateUser(id, request.toServiceDto())
        val userResponse = UserResponse.from(updatedUser)
        return ResponseEntity.ok(userResponse)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<UserDeleteResponse> {
        userService.deleteUser(id)
        val deleteResponse = UserDeleteResponse(deleted = true, id = id )
        return ResponseEntity.ok(deleteResponse)
    }


}

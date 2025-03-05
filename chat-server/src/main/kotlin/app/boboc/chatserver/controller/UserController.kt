package app.boboc.chatserver.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {
    @GetMapping
    suspend fun getUsers(){}

    @GetMapping("/{userId}")
    suspend fun getUser(@PathVariable userId: Long){}

    @PostMapping
    suspend fun createUser(){}

    @PutMapping("/{userId}")
    suspend fun updateUser(@PathVariable userId: Long){}

    @DeleteMapping("/{userId}")
    suspend fun deleteUser(@PathVariable userId: Long){}
}

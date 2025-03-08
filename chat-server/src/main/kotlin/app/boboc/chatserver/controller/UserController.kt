package app.boboc.chatserver.controller

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.service.UserService
import jakarta.validation.Valid
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
class UserController(
    private val userService: UserService,
) {
    @GetMapping()
    suspend fun getUsers(): List<Responses.User> = userService.getUsers()

    @GetMapping("/{userId}")
    suspend fun getUser(@PathVariable userId: Long): Responses.UserDetail = userService.getUser(userId)

    @PostMapping
    suspend fun createUser(@RequestBody @Valid req: Requests.User) = userService.registerUser(req)

    @PutMapping("/{userId}")
    suspend fun updateUser(@PathVariable userId: Long, @RequestBody @Valid req: Requests.User) =
        userService.updateUser(userId, req)

    @DeleteMapping("/{userId}")
    suspend fun deleteUser(@PathVariable userId: Long) = userService.deleteUser(userId)
}

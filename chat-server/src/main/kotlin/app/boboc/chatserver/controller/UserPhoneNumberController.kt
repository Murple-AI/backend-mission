package app.boboc.chatserver.controller

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.service.UserPhoneNumberService
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
@RequestMapping("users/{userId}/phone-numbers")
class UserPhoneNumberController(
    private val userPhoneNumberService: UserPhoneNumberService,
) {
    @GetMapping
    suspend fun getPhoneNumbers(@PathVariable userId: Long): List<Responses.PhoneNumber> =
        userPhoneNumberService.getPhoneNumbers(userId)

    @GetMapping("/{phoneNumberId}")
    suspend fun getPhoneNumber(@PathVariable userId: Long, @PathVariable phoneNumberId: Long): Responses.PhoneNumber =
        userPhoneNumberService.getPhoneNumber(userId, phoneNumberId)

    @PostMapping
    suspend fun createPhoneNumber(
        @PathVariable userId: Long,
        @RequestBody @Valid req: Requests.PhoneNumber,
    ) = userPhoneNumberService.registerUserPhoneNumber(userId, req)

    @PutMapping("/{phoneNumberId}")
    suspend fun updatePhoneNumber(
        @PathVariable userId: Long,
        @PathVariable phoneNumberId: Long,
        @RequestBody @Valid req: Requests.PhoneNumber,
    ) = userPhoneNumberService.updateUserPhoneNumber(userId, phoneNumberId, req)

    @DeleteMapping("/{phoneNumberId}")
    suspend fun deletePhoneNumber(
        @PathVariable userId: Long,
        @PathVariable phoneNumberId: Long,
    ) = userPhoneNumberService.deleteUserPhoneNumber(userId, phoneNumberId)
}

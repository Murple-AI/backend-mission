package app.boboc.chatserver.controller

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.service.UserAddressService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/addresses")
class UserAddressController(
    private val userAddressService: UserAddressService
) {
    @GetMapping
    suspend fun getAddresses(@PathVariable userId: Long): List<Responses.Address> =
        userAddressService.getUserAddresses(userId)

    @GetMapping("/{addressId}")
    suspend fun getAddress(@PathVariable userId: Long, @PathVariable addressId: Long): Responses.Address =
        userAddressService.getUserAddress(userId, addressId)

    @PostMapping
    suspend fun createAddress(@PathVariable userId: Long, @RequestBody @Valid req: Requests.Address) =
        userAddressService.registerUserAddress(userId, req)

    @PutMapping("/{addressId}")
    suspend fun updateAddress(
        @PathVariable userId: Long,
        @PathVariable addressId: Long,
        @RequestBody @Valid req: Requests.Address
    ) = userAddressService.updateUserAddress(userId, addressId, req)

    @DeleteMapping("/{addressId}")
    suspend fun deleteAddress(@PathVariable userId: Long, @PathVariable addressId: Long) =
        userAddressService.deleteUserAddress(userId, addressId)
}

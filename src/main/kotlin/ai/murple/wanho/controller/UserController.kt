package ai.murple.wanho.controller

import ai.murple.wanho.dto.UserRequestDto
import ai.murple.wanho.dto.UserResponseDto
import ai.murple.wanho.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Tag(name = "User API", description = "사용자 관리 API")
@RestController
@RequestMapping(value = ["/user"], produces = ["application/json"])
class UserController(private val userService: UserService) {

    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @PostMapping
    fun createUser(@RequestBody userRequestDto: UserRequestDto): ResponseEntity<Long?> {
        return ResponseEntity.ok(
            userService.createUser(
                userRequestDto.toUserInfoDto(),
                userRequestDto.toUserAddrDtoList(),
                userRequestDto.toUserTelDtoList()
            )
        )
    }

    @Operation(summary = "사용자 조회", description = "사용자를 조회합니다.")
    @GetMapping("/{idx}")
    fun readActiveUser(@PathVariable idx: Long): ResponseEntity<UserResponseDto> {
        return ResponseEntity.ok(userService.readActiveUser(idx).toResponse())
    }

    @Operation(summary = "사용자 수정", description = "사용자를 수정합니다.")
    @PutMapping("/{idx}")
    fun updateUser(@PathVariable idx: Long, @RequestBody userRequestDto: UserRequestDto): ResponseEntity<Long> {
        return ResponseEntity.ok(
            userService.updateUser(
                idx,
                userRequestDto.toUserInfoDto(),
                userRequestDto.toUserAddrDtoList(),
                userRequestDto.toUserTelDtoList()
            )
        )
    }

    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @DeleteMapping("/{idx}")
    fun deleteUser(@PathVariable idx: Long): ResponseEntity<Boolean> {
        return ResponseEntity.ok(userService.deleteUser(idx))
    }


}
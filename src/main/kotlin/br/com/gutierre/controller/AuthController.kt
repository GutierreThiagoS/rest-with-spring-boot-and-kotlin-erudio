package br.com.gutierre.controller

import br.com.gutierre.data.vo.v1.AccountCredentialVO
import br.com.gutierre.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "Authentication EndPoint",
    description = "Endpoints for Managing Authentication"
)
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authService: AuthService

    @Operation(
        summary = "Authenticates user",
        description = "Authenticates an user and return a token",
        tags = ["AccountCredential"],
    )
    @PostMapping(value = ["/signin"])
    fun signin(@RequestBody data: AccountCredentialVO?): ResponseEntity<*> {
        println("data $data")
        return if (data?.userName.isNullOrBlank() || data?.password.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        } else authService.signin(data!!)
    }

    @Operation(
        summary = "Refresh token",
        description = "Refresh token for Authenticated an user and returns a token",
        tags = ["RefreshToken"],
    )
    @PostMapping(value = ["/refresh/{username}"])
    fun refreshToken(
        @PathVariable("username") username: String?,
        @RequestHeader("Authorization") refreshToken: String?
    ): ResponseEntity<*> {
        return if (refreshToken.isNullOrBlank() || username.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        } else authService.refreshToken(username, refreshToken)
    }
}
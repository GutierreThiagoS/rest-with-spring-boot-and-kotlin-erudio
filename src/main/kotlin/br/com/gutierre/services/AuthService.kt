package br.com.gutierre.services


import br.com.gutierre.data.vo.v1.AccountCredentialVO
import br.com.gutierre.data.vo.v1.TokenVO
import br.com.gutierre.repository.UserRepository
import br.com.gutierre.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class AuthService {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager
    @Autowired
    lateinit var tokenProvider: JwtTokenProvider
    @Autowired
    lateinit var repository: UserRepository

    private val logger = Logger.getLogger(AuthService::class.java.name)

    fun signin(data: AccountCredentialVO): ResponseEntity<*> {
        logger.info("Trying log user ${data.userName}")
        return try {
            val username = data.userName
            val password = data.password
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            val user = repository.findByUserName(username)
            println("user $user")

            val tokenResponse: TokenVO = if(user != null) {
                tokenProvider.createAccessToken(username!!, user.roles)
            } else {
                throw UsernameNotFoundException("Username $username not found!")
            }
            ResponseEntity.ok(tokenResponse)
        } catch (e: AuthenticationException) {
            println("ERROR -> $e")
           throw BadCredentialsException("Invalid username or password supplied!")
        }
    }

    fun refreshToken(username: String, refreshToken: String): ResponseEntity<*> {
        logger.info("trying get refresh token to user $username")

        val user = repository.findByUserName(username)
        val tokenResponse: TokenVO = if(user != null) {
            tokenProvider.refreshToken(refreshToken)
        } else {
            throw UsernameNotFoundException("Username $username not found!")
        }
        return ResponseEntity.ok(tokenResponse)
    }
}
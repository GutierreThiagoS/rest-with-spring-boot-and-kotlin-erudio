package br.com.gutierre.services

import br.com.gutierre.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UserService(
    @field:Autowired var repository: UserRepository
): UserDetailsService {

    private val logger = Logger.getLogger(UserService::class.java.name)

    override fun loadUserByUsername(username: String?): UserDetails {
        logger.info("Finding one User with Username $username!")
        val user = repository.findByUserName(username)
        logger.info("User NAME  ${user?.userName} !!!")
        return (user as UserDetails?) ?: throw UsernameNotFoundException("UserName $username not found")
    }

}
package br.com.gutierre.data.vo.v1

import java.util.Date

data class TokenVO(
    val userName: String? = null,
    val authenticated: Boolean? = null,
    val created: Date? = null,
    val expiration: Date? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

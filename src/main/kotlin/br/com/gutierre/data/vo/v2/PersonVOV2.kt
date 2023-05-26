package br.com.gutierre.data.vo.v2

import java.util.Date

data class PersonVOV2 (
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = "",
    var birthDay: Date? = null
)

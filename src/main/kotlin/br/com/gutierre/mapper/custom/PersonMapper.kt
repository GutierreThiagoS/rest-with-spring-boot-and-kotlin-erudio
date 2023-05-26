package br.com.gutierre.mapper.custom

import br.com.gutierre.data.vo.v2.PersonVOV2
import br.com.gutierre.model.Person
import org.springframework.stereotype.Service
import java.util.Date

@Service
class PersonMapper {

    fun mapEntityToVO(person: Person): PersonVOV2 {
        val vo = PersonVOV2()
        vo.firstName = person.firstName
        vo.lastName = person.lastName
        vo.address = person.address
        vo.gender = person.gender
        vo.birthDay = Date()

        return vo
    }

    fun mapVOToEntity(person: PersonVOV2): Person {
        val entity = Person()
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
        //entity.birthDay = person.birthDay

        return entity
    }
}
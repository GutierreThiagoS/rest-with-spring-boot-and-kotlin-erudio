package br.com.gutierre.services

import br.com.gutierre.controller.PersonController
import br.com.gutierre.data.vo.v1.PersonVO
import br.com.gutierre.data.vo.v2.PersonVOV2
import br.com.gutierre.exceptions.RequiredObjectIsNullException
import br.com.gutierre.exceptions.ResourceNotFoundException
import br.com.gutierre.mapper.DozerMapper
import br.com.gutierre.mapper.custom.PersonMapper
import br.com.gutierre.model.Person
import br.com.gutierre.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findById(id: Long): PersonVO {
        logger.info("Finding one PersonVO with ID $id!")
        val person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No Records found for this ID!") }
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people")
        val persons = repository.findAll()
        val vos = DozerMapper.parseListObjects(persons, PersonVO::class.java)

        vos.forEach { personVO ->
            val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
            personVO.add(withSelfRel)
        }

        return vos
    }

    fun create(person: PersonVO?): PersonVO {
        logger.info("Create one person with name ${person?.firstName}")

        if (person == null) throw RequiredObjectIsNullException()

        val entity: Person = DozerMapper.parseObject(person, Person::class.java)

        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    /** Para uma segunda versão da aplicação
     * */
    fun createV2(person: PersonVOV2): PersonVOV2 {
        logger.info("Create one person with name ${person.firstName}")

        val entity: Person = DozerMapper.parseObject(person, Person::class.java)

        return mapper.mapEntityToVO(repository.save(entity))
    }

    fun update(person: PersonVO?): PersonVO {

        logger.info("Update one PersonVO with ID ${person?.key}")

        if (person == null) throw RequiredObjectIsNullException()

        val entity = repository.findById(person.key)
            .orElseThrow { ResourceNotFoundException("No Records found for this ID!") }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun delete(id: Long) {

        logger.info("Delete one person with ID $id")


        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No Records found for this ID!") }

        repository.delete(entity)
    }

}
package br.com.gutierre.repository

import br.com.gutierre.model.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository: JpaRepository<Person, Long?>
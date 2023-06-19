package br.com.gutierre.repository

import br.com.gutierre.model.Books
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<Books, Long?>
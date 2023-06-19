package br.com.gutierre.services

import br.com.gutierre.controller.BookController
import br.com.gutierre.data.vo.v1.BookVO
import br.com.gutierre.exceptions.RequiredObjectIsNullException
import br.com.gutierre.exceptions.ResourceNotFoundException
import br.com.gutierre.mapper.DozerMapper
import br.com.gutierre.model.Books
import br.com.gutierre.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class BookService {

    @Autowired
    private lateinit var repository: BookRepository

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(): List<BookVO> {
        logger.info("Finding all people")
        val books = repository.findAll()
        val vos = DozerMapper.parseListObjects(books, BookVO::class.java)

        vos.forEach { book ->
            val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(book.key).withSelfRel()
            book.add(withSelfRel)
        }

        return vos
    }

    fun findById(id: Long): BookVO {
        logger.info("Finding one PersonVO with ID $id!")
        val book = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No Records found for this ID!") }
        val bookVO: BookVO = DozerMapper.parseObject(book, BookVO::class.java)

        val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun create(book: BookVO?): BookVO {
        logger.info("Create one person with name ${book?.author}")

        if (book == null) throw RequiredObjectIsNullException()

        val entity: Books = DozerMapper.parseObject(book, Books::class.java)

        val bookVO: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)

        val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun update(book: BookVO?): BookVO {

        logger.info("Update one Book with ID ${book?.key}")

        if (book == null) throw RequiredObjectIsNullException()

        val entity = repository.findById(book.key)
            .orElseThrow { ResourceNotFoundException("No Records found for this ID!") }

        entity.author = book.author
        entity.launchDate = book.launchDate
        entity.price = book.price
        entity.title = book.title

        val bookVO: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)

        val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun delete(id: Long) {

        logger.info("Delete one book with ID $id")

        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No Records found for this ID!") }

        repository.delete(entity)
    }
}
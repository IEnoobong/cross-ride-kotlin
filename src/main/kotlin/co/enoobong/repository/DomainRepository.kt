package co.enoobong.repository

import co.enoobong.model.Person
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface PersonRepository : PagingAndSortingRepository<Person, Long> {

    override fun findAll(): List<Person>
}
package co.enoobong.service

import co.enoobong.model.Person

interface PersonService {

    fun getAll(): List<Person>

    fun save(person: Person): Person

    fun findById(personId: Long): Person
}
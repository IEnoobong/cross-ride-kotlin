package co.enoobong.service

import co.enoobong.exception.ResourceNotFoundException
import co.enoobong.model.Person
import co.enoobong.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonServiceImpl(private val personRepository: PersonRepository) : PersonService {

    override fun getAll(): List<Person> {
        return personRepository.findAll()
    }

    override fun save(person: Person): Person {
        return personRepository.save(person)
    }

    override fun findById(personId: Long): Person {
        return personRepository.findById(personId).orElseThrow {
            ResourceNotFoundException(
                "Person", "personId",
                personId
            )
        }
    }
}
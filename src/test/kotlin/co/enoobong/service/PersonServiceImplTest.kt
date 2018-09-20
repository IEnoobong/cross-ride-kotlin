package co.enoobong.service

import co.enoobong.exception.ResourceNotFoundException
import co.enoobong.model.Person
import co.enoobong.repository.PersonRepository
import co.enoobong.util.PERSON_ID
import co.enoobong.util.createValidPerson
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class PersonServiceImplTest {

    private val personRepository: PersonRepository = mock()
    private val personService: PersonService = PersonServiceImpl(personRepository)

    @Test
    fun getAll() {
        val person = createValidPerson()
        val persons = listOf(person)
        whenever(personRepository.findAll()).thenReturn(persons)

        val gottenPersons = personService.getAll()

        assertSame(gottenPersons[0], persons[0])
        verify(personRepository, times(1)).findAll()

    }

    @Test
    fun save() {
        val person = createValidPerson()
        whenever(personRepository.save(person)).thenReturn(person)

        val savedPerson = personService.save(person)

        assertSame(person, savedPerson)
        verify(personRepository, times(1)).save(person)
    }

    @Test
    fun findById() {
        val person = createValidPerson()
        val personOptional = Optional.of(person)
        whenever(personRepository.findById(PERSON_ID)).thenReturn(personOptional)

        val foundPerson = personService.findById(PERSON_ID)

        assertSame(person, foundPerson)
        verify(personRepository, times(1)).findById(PERSON_ID)
    }

    @Nested
    inner class ErrorCase {

        @Test
        fun `find person by id not found`() {
            val personOptional = Optional.empty<Person>()
            whenever(personRepository.findById(PERSON_ID)).thenReturn(personOptional)

            val exception = assertThrows<ResourceNotFoundException> {
                personService.findById(PERSON_ID)
            }
            assertEquals("Person not found with personId : $PERSON_ID", exception.message)
            verify(personRepository, times(1)).findById(PERSON_ID)
        }
    }
}
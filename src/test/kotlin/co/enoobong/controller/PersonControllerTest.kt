package co.enoobong.controller

import co.enoobong.config.GeneralConfig
import co.enoobong.service.PersonService
import co.enoobong.util.PERSON_ID
import co.enoobong.util.convertToJsonBytes
import co.enoobong.util.createValidPerson
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PersonController::class)
@ExtendWith(SpringExtension::class)
@Import(value = [GeneralConfig::class])
class PersonControllerTest(@Autowired private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var personService: PersonService

    @Test
    fun registerPerson() {
        val person = createValidPerson()
        whenever(personService.save(person)).thenReturn(person)

        mockMvc.perform(
            post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(person.convertToJsonBytes())
        )
            .andExpect(status().isOk)
            .andExpect(content().string(notNullValue()))

    }

    @Test
    fun getAllPersons() {
        val person = createValidPerson()
        val people = listOf(person)
        whenever(personService.getAll()).thenReturn(people)

        mockMvc.perform(get("/api/person").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.[0].name").value(person.name))
            .andExpect(jsonPath("\$.[0].email").value(person.email))

    }


    @Test
    fun findPersonById() {
        val person = createValidPerson()
        whenever(personService.findById(PERSON_ID)).thenReturn(person)

        // Act & Assert
        mockMvc.perform(get("/api/person/$PERSON_ID").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.name").value(person.name))
            .andExpect(jsonPath("\$.email").value(person.email))
    }

}
package co.enoobong.controller

import co.enoobong.model.Person
import co.enoobong.service.PersonService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/person", produces = [MediaType.APPLICATION_JSON_VALUE])
class PersonController(private val personService: PersonService) {

    @PostMapping
    fun register(@RequestBody @Valid person: Person): ResponseEntity<Person> {
        val savedPerson = personService.save(person)
        return ResponseEntity.ok(savedPerson)
    }

    @GetMapping
    fun getAllPersons(): ResponseEntity<List<Person>> {
        val allPersons = personService.getAll()
        return ResponseEntity.ok(allPersons)
    }

    @GetMapping(path = ["/{person-id}"])
    fun getPersonById(@PathVariable(name = "person-id") personId: Long): ResponseEntity<Person> {
        val person = personService.findById(personId)
        return ResponseEntity.ok(person)
    }
}
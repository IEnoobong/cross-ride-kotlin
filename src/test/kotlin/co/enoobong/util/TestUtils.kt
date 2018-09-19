package co.enoobong.util

import co.enoobong.model.Person
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.time.LocalDateTime
import java.util.*


fun Any.convertToJsonBytes(): ByteArray {
    val mapper = ObjectMapper()
    mapper.registerModule(JavaTimeModule())
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
    return mapper.writeValueAsBytes(this)
}


fun Any.convertToJsonString(): String {
    val mapper = ObjectMapper()
    mapper.registerModule(JavaTimeModule())
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
    return mapper.writeValueAsString(this)
}

const val PERSON_ID = 1L

fun createValidPerson(): Person {
    val person = Person(PERSON_ID, "Eno", "ibanga@enoobong.co")
    person.registrationNumber = UUID.randomUUID().toString()
    person.registrationDate = LocalDateTime.now()
    return person
}
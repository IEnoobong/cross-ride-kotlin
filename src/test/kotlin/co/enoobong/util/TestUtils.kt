package co.enoobong.util

import co.enoobong.dto.RideDTO
import co.enoobong.dto.TopDriverDTO
import co.enoobong.model.Person
import co.enoobong.model.Ride
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
const val DRIVER_ID = 1L
const val RIDER_ID = 2L
const val RIDE_ID = 1L

val mockTime: LocalDateTime = LocalDateTime.parse("2018-09-11T09:52:28")

fun createValidPerson(): Person {
    val person = Person(PERSON_ID, "Eno", "ibanga@enoobong.co")
    person.registrationNumber = UUID.randomUUID().toString()
    person.registrationDate = LocalDateTime.now()
    return person
}

fun createRideDTO(startTime: LocalDateTime = mockTime, endTime: LocalDateTime = mockTime): RideDTO {
    val rideDTO = RideDTO(20L, DRIVER_ID, RIDER_ID)
    rideDTO.endTime = endTime
    rideDTO.startTime = startTime
    return rideDTO
}

fun createValidRide(startTime: LocalDateTime = mockTime, endTime: LocalDateTime = mockTime): Ride {
    val person = createValidPerson()
    val ride = Ride(startTime, endTime, 1L, person, person)
    ride.id = RIDE_ID
    return ride
}

class TopDriverDTOStub : TopDriverDTO {
    private val stringValue = "String"
    private val longValue = 1L
    private val doubleValue = 2.0

    override fun getName(): String {
        return stringValue
    }

    override fun getEmail(): String {
        return stringValue
    }

    override fun getTotalRideDurationInMins(): Long {
        return longValue
    }

    override fun getMaxRideDurationInMins(): Long {
        return longValue
    }

    override fun getAverageDistance(): Double {
        return doubleValue
    }

}
package co.enoobong.service

import co.enoobong.exception.InvalidTimeException
import co.enoobong.exception.ResourceNotFoundException
import co.enoobong.model.Person
import co.enoobong.model.Ride
import co.enoobong.repository.PersonRepository
import co.enoobong.repository.RideRepository
import co.enoobong.util.DRIVER_ID
import co.enoobong.util.PERSON_ID
import co.enoobong.util.RIDER_ID
import co.enoobong.util.RIDE_ID
import co.enoobong.util.TopDriverDTOStub
import co.enoobong.util.createRideDTO
import co.enoobong.util.createValidPerson
import co.enoobong.util.createValidRide
import co.enoobong.util.mockTime
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
import java.time.LocalDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class RideServiceImplTest {

    private val personRepository = mock<PersonRepository>()
    private val rideRepository = mock<RideRepository>()

    private val riderService = RideServiceImpl(rideRepository, personRepository)

    @Test
    fun save() {
        val rideDTO = createRideDTO(endTime = LocalDateTime.now())
        val person = createValidPerson()
        val personOptional = Optional.of(person)
        val ride = rideDTO.toRide(person, person)

        whenever(personRepository.findById(RIDER_ID)).thenReturn(personOptional)
        whenever(personRepository.findById(DRIVER_ID)).thenReturn(personOptional)
        whenever(rideRepository.save(ride)).thenReturn(ride)

        val savedRide = riderService.save(rideDTO)

        assertSame(ride, savedRide)
        verify(personRepository, times(1)).findById(PERSON_ID)
        verify(personRepository, times(1)).findById(DRIVER_ID)
        verify(rideRepository, times(1)).save(ride)


    }

    @Test
    fun findById() {
        val ride = createValidRide()
        val optionalOfRide = Optional.of(ride)
        whenever(rideRepository.findById(RIDE_ID)).thenReturn(optionalOfRide)

        val gottenRide = riderService.findById(RIDE_ID)

        assertSame(ride, gottenRide)
        verify(rideRepository, times(1)).findById(RIDE_ID)
    }

    @Test
    fun getTopDrivers() {
        val topDriverDTO = TopDriverDTOStub()
        val topDrivers = listOf(topDriverDTO)
        val count = 1L
        whenever(rideRepository.getTopDrivers(count, mockTime, mockTime)).thenReturn(topDrivers)

        val gottenTopDrivers = riderService.getTopDrivers(count, mockTime, mockTime)

        assertSame(topDrivers[0], gottenTopDrivers[0])
        verify(rideRepository, times(1)).getTopDrivers(count, mockTime, mockTime)
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_METHOD)
    inner class ErrorCases {

        @Test
        fun `find ride by id not found`() {
            val optionalOfRide = Optional.empty<Ride>()
            whenever(rideRepository.findById(RIDE_ID)).thenReturn(optionalOfRide)

            val exception = assertThrows<ResourceNotFoundException> {
                riderService.findById(RIDE_ID)
            }
            assertEquals("Ride not found with rideId : $RIDE_ID", exception.message)
            verify(rideRepository, times(1)).findById(RIDE_ID)
        }

        @Test
        fun `save ride when invalid endTime`() {
            val rideDTO = createRideDTO()

            val exception = assertThrows<InvalidTimeException> {
                riderService.save(rideDTO)
            }

            assertEquals(
                "endTime : ${rideDTO.endTime} cannot be before or equal to startTime : ${rideDTO
                    .startTime}", exception.message
            )
        }

        @Test
        fun `save ride when driver not found`() {
            val rideDTO = createRideDTO(endTime = LocalDateTime.now())
            val personOptional = Optional.empty<Person>()


            whenever(personRepository.findById(DRIVER_ID)).thenReturn(personOptional)

            val exception = assertThrows<ResourceNotFoundException> {
                riderService.save(rideDTO)
            }

            assertEquals("Driver not found with driverId : $DRIVER_ID", exception.message)
            verify(personRepository, times(1)).findById(DRIVER_ID)
        }

        @Test
        fun `save ride when rider not found`() {
            val rideDTO = createRideDTO(endTime = LocalDateTime.now())
            val emptyPersonOptional = Optional.empty<Person>()
            val person = createValidPerson()
            val personOptional = Optional.of(person)

            whenever(personRepository.findById(DRIVER_ID)).thenReturn(personOptional)
            whenever(personRepository.findById(RIDER_ID)).thenReturn(emptyPersonOptional)

            val exception = assertThrows<ResourceNotFoundException> {
                riderService.save(rideDTO)
            }

            assertEquals("Rider not found with riderId : $RIDER_ID", exception.message)
            verify(personRepository, times(1)).findById(DRIVER_ID)
            verify(personRepository, times(1)).findById(RIDER_ID)
        }
    }
}
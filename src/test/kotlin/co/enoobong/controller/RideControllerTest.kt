package co.enoobong.controller

import co.enoobong.config.GeneralConfig
import co.enoobong.dto.RideDTO
import co.enoobong.dto.TopDriverDTO
import co.enoobong.model.Ride
import co.enoobong.service.RideService
import co.enoobong.util.convertToJsonString
import co.enoobong.util.createValidPerson
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@WebMvcTest(RideController::class)
@Import(value = [GeneralConfig::class])
class RideControllerTest(@Autowired private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var rideService: RideService

    @Value("\${distance.compulsory}")
    private lateinit var distanceErrorMessage: String

    @Value("\${driverId.compulsory}")
    private lateinit var driverIdErrorMessage: String

    @Value("\${riderId.compulsory}")
    private lateinit var riderIdErrorMessage: String

    private val DRIVER_ID = 1L
    private val RIDER_ID = 2L
    private val RIDE_ID = 1L
    private val mockTime = LocalDateTime.parse("2018-09-11T09:52:28")

    @Test
    fun createNewRide() {
        val rideDTO = createValidRideDTO()
        val person = createValidPerson()
        val ride = rideDTO.toRide(person, person)
        whenever(rideService.save(any())).thenReturn(ride)

        mockMvc.perform(post("/api/ride").contentType(MediaType.APPLICATION_JSON).content(rideDTO.convertToJsonString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.startTime").value(ride.startTime.toString()))
            .andExpect(jsonPath("\$.distance").value(ride.distance))
            .andExpect(jsonPath("\$.rider.name").value(ride.rider.name))

    }

    @Test
    fun `attempt to create invalid ride should return bad request`() {
        val rideDTO = createValidRideDTO()
        rideDTO.distance = -1
        rideDTO.driverId = 0
        rideDTO.riderId = 0

        mockMvc.perform(post("/api/ride").contentType(MediaType.APPLICATION_JSON).content(rideDTO.convertToJsonString()))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("\$.driverId").value(driverIdErrorMessage))
            .andExpect(jsonPath("\$.riderId").value(riderIdErrorMessage))
            .andExpect(jsonPath("\$.distance").value(distanceErrorMessage))
    }

    @Test
    fun getRideById() {
        val ride = createValidRide()
        whenever(rideService.findById(RIDE_ID)).thenReturn(ride)

        mockMvc.perform(get("/api/ride/$RIDE_ID").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.startTime").value(ride.startTime.toString()))
            .andExpect(jsonPath("\$.id").value(ride.id!!))
            .andExpect(jsonPath("\$.driver.name").value(ride.driver.name))
    }

    @Test
    fun getTopDriver() {
        val count = 5L
        val topDriverDTO = TopDriverDTOStub()
        val topDriverDTOS = listOf<TopDriverDTO>(topDriverDTO)
        whenever(rideService.getTopDrivers(count, mockTime, mockTime)).thenReturn(topDriverDTOS)

        // Act & Assert
        mockMvc.perform(get("/api/top-rides/?startTime=$mockTime&endTime=$mockTime"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.[0].email").value(topDriverDTO.getEmail()))
            .andExpect(jsonPath("\$.[0].totalRideDurationInMins").value(topDriverDTO.getTotalRideDurationInMins()))
    }

    private fun createValidRide(): Ride {
        val person = createValidPerson()
        val ride = Ride(mockTime, mockTime, 1L, person, person)
        ride.id = RIDE_ID
        return ride
    }

    private fun createValidRideDTO(): RideDTO {
        val rideDTO = RideDTO(20L, DRIVER_ID, RIDER_ID)
        rideDTO.endTime = mockTime
        rideDTO.startTime = mockTime
        return rideDTO
    }

    private class TopDriverDTOStub : TopDriverDTO {
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
}
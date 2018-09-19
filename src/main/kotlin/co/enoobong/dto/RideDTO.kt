package co.enoobong.dto

import co.enoobong.model.Person
import co.enoobong.model.Ride
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

class RideDTO(
    @field:NotNull(message = "startTime is compulsory")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @field:Past(message = "startTime must be a time in the past")
    val startTime: LocalDateTime? = null,

    @field:NotNull(message = "endTime is compulsory")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endTime: LocalDateTime? = null,

    @field:NotNull(message = "distance is compulsory")
    @field:Min(value = 1, message = "distance must be greater than 0")
    val distance: Long? = null,

    @field:NotNull(message = "driverId id is compulsory")
    val driverId: Long? = null,

    @field:NotNull(message = "riderId id is compulsory")
    val riderId: Long? = null
) {
    fun toRide(driver: Person, rider: Person): Ride {
        return Ride(startTime!!, endTime!!, distance!!, driver, rider)
    }

    override fun toString(): String {
        return "RideDTO(startTime=$startTime, endTime=$endTime, distance=$distance, driverId=$driverId, riderId=$riderId)"
    }


}
package co.enoobong.dto

import co.enoobong.model.Person
import co.enoobong.model.Ride
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

class RideDTO(
    @field:Min(value = 1, message = "distance is compulsory and must be greater than zero")
    var distance: Long,

    @field:Min(value = 1, message = "driverId is compulsory")
    var driverId: Long,

    @field:Min(value = 1, message = "riderId is compulsory")
    var riderId: Long
) {

    @field:NotNull(message = "startTime is compulsory")
    @field:Past(message = "startTime must be a time in the past")
    lateinit var startTime: LocalDateTime

    @field:NotNull(message = "endTime is compulsory")
    lateinit var endTime: LocalDateTime

    fun toRide(driver: Person, rider: Person): Ride {
        return Ride(startTime, endTime, distance, driver, rider)
    }

    override fun toString(): String {
        return "RideDTO(startTime=$startTime, endTime=$endTime, distance=$distance, driverId=$driverId, riderId=$riderId)"
    }


}
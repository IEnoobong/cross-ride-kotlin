package co.enoobong.service

import co.enoobong.dto.RideDTO
import co.enoobong.dto.TopDriverDTO
import co.enoobong.model.Ride
import java.time.LocalDateTime

interface RideService {
    fun save(rideDTO: RideDTO): Ride

    fun findById(rideId: Long): Ride

    fun getTopDrivers(count: Long, startTime: LocalDateTime, endTime: LocalDateTime): List<TopDriverDTO>
}
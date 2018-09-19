package co.enoobong.service

import co.enoobong.dto.RideDTO
import co.enoobong.dto.TopDriverDTO
import co.enoobong.exception.InvalidTimeException
import co.enoobong.exception.ResourceNotFoundException
import co.enoobong.model.Ride
import co.enoobong.repository.PersonRepository
import co.enoobong.repository.RideRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RideServiceImpl(private val rideRepository: RideRepository, private val personRepository: PersonRepository) :
    RideService {

    override fun save(rideDTO: RideDTO): Ride {
        val startTime = rideDTO.startTime!!
        val endTime = rideDTO.endTime!!

        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw InvalidTimeException("endTime $endTime cannot be before or equal to startTime : $startTime")
        }

        val driver = personRepository.findById(rideDTO.driverId!!)
            .orElseThrow { ResourceNotFoundException("Driver", "driverId", rideDTO.driverId) }

        val rider = personRepository.findById(rideDTO.riderId!!)
            .orElseThrow { ResourceNotFoundException("Rider", "riderId", rideDTO.riderId) }

        val ride = rideDTO.toRide(driver, rider)

        return rideRepository.save(ride)
    }

    override fun findById(rideId: Long): Ride {
        return rideRepository.findById(rideId).orElseThrow { ResourceNotFoundException("Ride", "rideId", rideId) }
    }

    override fun getTopDrivers(count: Long, startTime: LocalDateTime, endTime: LocalDateTime): List<TopDriverDTO> {
        return rideRepository.getTopDrivers(count, startTime, endTime)
    }
}
package co.enoobong.controller

import co.enoobong.dto.RideDTO
import co.enoobong.dto.TopDriverDTO
import co.enoobong.model.Ride
import co.enoobong.service.RideService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/api", produces = [MediaType.APPLICATION_JSON_VALUE])
class RideController(private val rideService: RideService) {

    @PostMapping(path = ["ride"])
    fun createNewRide(@RequestBody @Valid rideDTO: RideDTO, bindingResult: BindingResult): ResponseEntity<*> {
        if (bindingResult.hasFieldErrors()) {
            val errorFieldToMessage = bindingResult.fieldErrors.associateBy({ it.field }, { it.defaultMessage })
            return ResponseEntity.badRequest().body(errorFieldToMessage)
        }

        val savedRide = rideService.save(rideDTO)
        return ResponseEntity.ok(savedRide)
    }

    @GetMapping(path = ["ride/{ride-id}"])
    fun getRideById(@PathVariable(name = "ride-id") rideId: Long): Ride {
        return rideService.findById(rideId)
    }

    @RequestMapping("top-rides")
    fun getTopDriver(
        @RequestParam(value = "max", defaultValue = "5") count: Long,
        @RequestParam(value = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") startTime: LocalDateTime,
        @RequestParam(value = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") endTime: LocalDateTime
    ): ResponseEntity<List<TopDriverDTO>> {

        val topDrivers = rideService.getTopDrivers(count, startTime, endTime)

        return ResponseEntity.ok(topDrivers)
    }
}
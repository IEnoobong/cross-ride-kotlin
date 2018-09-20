package co.enoobong.exception

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Component
@ControllerAdvice
class ExceptionHandler(private val logger: Logger) {

    @ExceptionHandler
    fun handleGlobalException(exception: Exception): ResponseEntity<HashMap<String, String>> {
        logger.error("Exception: Unable to process this request. ", exception)

        val response = hashMapOf("message" to "Unable to process this request.")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class, InvalidTimeException::class])
    fun handleCrossRideExceptions(exception: RuntimeException): ResponseEntity<HashMap<String, String?>> {
        logger.warn(exception.message)

        val response = hashMapOf("message" to exception.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

}
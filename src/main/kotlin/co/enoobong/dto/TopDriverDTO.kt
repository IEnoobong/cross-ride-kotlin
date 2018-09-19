package co.enoobong.dto

interface TopDriverDTO {

    fun getName(): String

    fun getEmail(): String

    fun getTotalRideDurationInMins(): Long

    fun getMaxRideDurationInMins(): Long

    fun getAverageDistance(): Double
}

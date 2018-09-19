package co.enoobong.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "person", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("registration_number"))])
@JsonIgnoreProperties(value = ["registrationNumber", "registrationDate"], allowGetters = true)
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @NotBlank
    @Column(name = "name")
    val name: String,
    @NotNull
    @Email
    @Column(name = "email", unique = true)
    val email: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 7401548380514451401L
    }

    @Column(name = "registration_number", unique = true, nullable = false, updatable = false)
    var registrationNumber: String = ""

    @Column(name = "registration_date", nullable = false, updatable = false)
    var registrationDate: LocalDateTime = LocalDateTime.now()

    @PrePersist
    fun prePersist() {
        registrationNumber = UUID.randomUUID().toString()
        registrationDate = LocalDateTime.now()
    }


    override fun toString(): String {
        return "Person(id=$id, name='$name', email='$email', registrationNumber='$registrationNumber')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (registrationNumber != other.registrationNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + registrationNumber.hashCode()
        return result
    }
}

@Entity
@Table(name = "ride")
class Ride(
    @NotNull
    @Column(name = "start_time")
    val startTime: LocalDateTime,
    @NotNull
    @Column(name = "end_time")
    val endTime: LocalDateTime,
    @NotNull
    @Column(name = "distance")
    val distance: Long,
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    val driver: Person,
    @ManyToOne
    @JoinColumn(name = "rider_id", referencedColumnName = "id")
    val rider: Person
) : Serializable {
    companion object {
        private const val serialVersionUID = 9097639215351514001L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ride

        if (id != other.id) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (distance != other.distance) return false
        if (driver != other.driver) return false
        if (rider != other.rider) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        result = 31 * result + distance.hashCode()
        result = 31 * result + driver.hashCode()
        result = 31 * result + rider.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


    override fun toString(): String {
        return "Ride(id=$id, startTime=$startTime, endTime=$endTime, distance=$distance, driverId=$driver, riderId=$rider)"
    }

}
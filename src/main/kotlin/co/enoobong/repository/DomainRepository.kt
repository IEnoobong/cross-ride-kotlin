package co.enoobong.repository

import co.enoobong.dto.TopDriverDTO
import co.enoobong.model.Person
import co.enoobong.model.Ride
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.time.LocalDateTime

@RepositoryRestResource(exported = false)
interface PersonRepository : PagingAndSortingRepository<Person, Long> {

    override fun findAll(): List<Person>
}

@RepositoryRestResource(exported = false)
interface RideRepository : CrudRepository<Ride, Long> {
    @Query(
        nativeQuery = true,
        value = "select p.name, p.email, sum(TIMESTAMPDIFF(minute, r.start_time, r.end_time)) as totalRideDurationInMins, " +
                "max(TIMESTAMPDIFF(minute, r.start_time, r.end_time)) as maxRideDurationInMins, avg(r.distance) as averageDistance " +
                "from person p, ride r where p.id = r.driver_id and r.start_time >= :startTime and r.end_time >= " +
                ":endTime group by r.driver_id order by MAX(r.distance) DESC limit :howMany"
    )
    fun getTopDrivers(
        @Param("howMany") howMany: Long,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<TopDriverDTO>
}
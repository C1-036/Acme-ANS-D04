
package acme.entities.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduledDeparture) from Leg l where l.flight.id = :flightId")
	Date findScheduledDeparture(int flightId);

	@Query("select max(l.scheduledArrival) from Leg l where l.flight.id = :flightId")
	Date findScheduledArrival(int flightId);

	@Query("select l.departureAirport from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	String findOriginCity(int flightId);

	@Query("select l.arrivalAirport from Leg l where l.flight.id = :flightId order by l.scheduledArrival desc")
	String findDestinationCity(int flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer countLegs(int flightId);

}

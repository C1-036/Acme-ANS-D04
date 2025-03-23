
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

}

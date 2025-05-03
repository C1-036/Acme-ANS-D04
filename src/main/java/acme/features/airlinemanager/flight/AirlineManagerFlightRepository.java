/*
 * 
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airlinemanager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT f FROM Flight f WHERE f.airlinemanager.id = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select count(fa) from FlightAssignment fa where fa.flightLeg.flight.id = :flightId")
	int countAssignmentsByFlightId(int flightId);

	@Query("SELECT COUNT(l1) > 0 FROM Leg l1, Leg l2 WHERE l1.flight.id = :flightId AND l2.flight.id = :flightId AND l1.scheduledArrival < l2.scheduledDeparture AND NOT EXISTS (SELECT l3 FROM Leg l3 WHERE l3.flight.id = :flightId AND l3.scheduledDeparture > l1.scheduledArrival AND l3.scheduledDeparture < l2.scheduledDeparture) AND l1.aircraft != l2.aircraft")
	boolean requiresSelfTransferBetweenConsecutiveLegs(int flightId);

}

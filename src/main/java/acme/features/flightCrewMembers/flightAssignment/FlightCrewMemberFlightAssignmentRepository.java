
package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.entities.flightCrewMembers.FlightDuty;
import acme.entities.flights.Leg;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

	// List my completed assignments
	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.flightLeg.scheduledArrival < :lastUpdate")
	Collection<FlightAssignment> findAllCompletedFlightAssignments(Date lastUpdate, int flightCrewMemberId);

	// List my planned assignments
	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.flightLeg.scheduledDeparture >= :lastUpdate")
	Collection<FlightAssignment> findAllPlannedFlightAssignments(Date lastUpdate, int flightCrewMemberId);

	// List published assignments
	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.draftMode = true")
	Collection<FlightAssignment> findAllPublishedFlightAssignments();

	// Show all the legs in the select choices
	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId AND l.draftMode = false")
	Collection<Leg> findAllLegsByAirlineId(int airlineId);

	// Find all the activity logs to remove the flight assignment
	@Query("SELECT a FROM ActivityLog a WHERE a.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> findAllActivityLogs(int flightAssignmentId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightLeg.id = :legId AND fa.duty = :duty")
	FlightAssignment findFlightAssignmentByLegAndDuty(int legId, FlightDuty duty);

	@Query("SELECT COUNT(fa) > 0 FROM FlightAssignment fa WHERE fa.flightLeg.id = :legId AND fa.duty IN ('PILOT', 'CO_PILOT') AND fa.duty = :duty AND fa.id != :id")
	boolean hasDutyAssigned(int legId, FlightDuty duty, int id);

	@Query("SELECT COUNT(fa) > 0 FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.lastUpdate >= :lastUpdate AND fa.draftMode = false")
	boolean hasFlightCrewMemberLegAssociated(int flightCrewMemberId, Date lastUpdate);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId")
	Collection<Leg> findAllLegsFromAirline(int airlineId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightLeg.flight.id=:flightId")
	Collection<FlightAssignment> findFlightAssignmentsByFlightId(int flightId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightLeg.id=:legId")
	Collection<FlightAssignment> findFlightAssignmentsByLegId(int legId);
}

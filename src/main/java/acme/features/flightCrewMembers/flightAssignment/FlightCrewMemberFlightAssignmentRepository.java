
package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.FlightCrewMembers;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> findAllFlightAssignments();

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.flightLeg.scheduledArrival < :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findCompletedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select fa from FlightAssignment fa where fa.flightLeg.scheduledDeparture > :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findPlannedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select m from FlightCrewMembers m")
	Collection<FlightCrewMembers> findAllFlightCrewMembers();

	@Query("select m from FlightCrewMembers m where m.id = :memberId")
	FlightCrewMembers findFlightCrewMemberById(int memberId);

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("select distinct fa.flightLeg from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("select fa from FlightAssignment fa where fa.flightLeg.id = :legId")
	Collection<FlightAssignment> findFlightAssignmentByLegId(int legId);
}


package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.entities.flightCrewMembers.FlightDuty;
import acme.entities.flights.Leg;
import acme.realms.FlightCrewMembers;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.flightLeg.scheduledArrival < CURRENT_TIMESTAMP and f.flightCrewMember.id = ?1")
	Collection<FlightAssignment> assignmentsWithCompletedLegs(Integer member);

	@Query("select f from FlightAssignment f where f.flightLeg.scheduledDeparture > CURRENT_TIMESTAMP and f.flightCrewMember.id = ?1")
	Collection<FlightAssignment> assignmentsWithPlannedLegs(Integer member);

	@Query("select f from FlightAssignment f where f.flightCrewMember.id = ?1")
	Collection<FlightAssignment> findAllAssignmentsByMemberId(Integer member);

	@Query("select f from FlightAssignment f where f.id = ?1")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.id = ?1")
	Leg findLegById(int id);

	@Query("select f.flightLeg from FlightAssignment f where f.flightCrewMember.id = ?1")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("SELECT l FROM Leg l where l.draftMode = false")
	Collection<Leg> findAllLegs();

	@Query("SELECT fcm FROM FlightCrewMembers fcm")
	Collection<FlightCrewMembers> findAllFlightCrewMembers();

	@Query("select al from ActivityLog al where al.flightAssignment.id = ?1")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("SELECT fcm FROM FlightCrewMembers fcm WHERE fcm.id = ?1")
	FlightCrewMembers findFlightCrewMemberById(int flightCrewMemberId);

	@Query("SELECT fa.flightLeg FROM FlightAssignment fa WHERE (fa.flightLeg.scheduledDeparture < :arrival AND fa.flightLeg.scheduledArrival > :departure) AND fa.flightLeg.id <> :legId AND fa.flightCrewMember.id = :flightCrewMemberId")
	List<Leg> findSimultaneousLegsByMemberId(Date departure, Date arrival, int legId, int flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightLeg = :flightAssignmentLeg and fa.duty = :duty")
	Collection<FlightAssignment> findFlightAssignmentByLegAndDuty(Leg flightAssignmentLeg, FlightDuty duty);

}

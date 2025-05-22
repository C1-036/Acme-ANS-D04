
package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.AssignmentStatus;
import acme.entities.flightCrewMembers.AvailabilityStatus;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.entities.flightCrewMembers.FlightDuty;
import acme.entities.flights.Leg;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		status = assignment.isDraftMode() && assignment != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "status", "remarks", "flightLeg");

		int id = super.getRequest().getData("id", int.class);
		FlightAssignment original = this.repository.findFlightAssignmentById(id);
		assignment.setFlightCrewMember(original.getFlightCrewMember());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		assert assignment != null;

		// Validar estado permitido para publicar
		AssignmentStatus status = assignment.getStatus();
		boolean canBePublished = status == AssignmentStatus.CONFIRMED || status == AssignmentStatus.CANCELLED;
		super.state(canBePublished, "status", "acme.validation.flight-assignment.status-not-publishable");

		Leg leg = assignment.getFlightLeg();
		int legId = leg.getId();
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Validar que el miembro está disponible
		boolean availableMember = this.repository.findFlightCrewMemberById(memberId).getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		super.state(availableMember, "flightCrewMember", "acme.validation.flightassignment.flightcrewmember.available.message");

		// Validar que no hay solapamiento con otros legs
		Date departure = leg.getScheduledDeparture();
		Date arrival = leg.getScheduledArrival();
		Collection<Leg> overlappingLegs = this.repository.findSimultaneousLegsByMemberId(departure, arrival, legId, memberId);
		boolean hasNoOverlappingLegs = overlappingLegs.isEmpty();
		super.state(hasNoOverlappingLegs, "flightLeg", "acme.validation.flightassignment.leg.overlap.message");

		// Validar que la leg no esté completada
		//		boolean legCompleted = MomentHelper.isBefore(arrival, MomentHelper.getCurrentMoment());
		//		super.state(!legCompleted, "leg", "acme.validation.flightassignment.leg.completed.message");

		// Validar que no haya más de 1 piloto
		Collection<FlightAssignment> pilotAssignments = this.repository.findFlightAssignmentByLegAndDuty(leg, FlightDuty.PILOT);
		boolean isPilot = assignment.getDuty() == FlightDuty.PILOT;
		boolean hasPilotSlotAvailable = !isPilot || pilotAssignments.stream().filter(a -> a.getId() != assignment.getId()).count() < 1;
		super.state(hasPilotSlotAvailable, "duty", "acme.validation.flightassignment.duty.pilot.message");

		// Validar que no haya más de 1 copiloto
		Collection<FlightAssignment> copilotAssignments = this.repository.findFlightAssignmentByLegAndDuty(leg, FlightDuty.CO_PILOT);
		boolean isCopilot = assignment.getDuty() == FlightDuty.CO_PILOT;
		boolean hasCopilotSlotAvailable = !isCopilot || copilotAssignments.stream().filter(a -> a.getId() != assignment.getId()).count() < 1;
		super.state(hasCopilotSlotAvailable, "duty", "acme.validation.flightassignment.duty.copilot.message");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		dutyChoice = SelectChoices.from(FlightDuty.class, assignment.getDuty());
		currentStatusChoice = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		legs = this.repository.findAllLegs();
		legChoice = SelectChoices.from(legs, "flightNumber", assignment.getFlightLeg());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "flightLeg", "draftMode");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("flightCrewMember.identity.fullName", assignment.getFlightCrewMember().getIdentity().getFullName());

		super.getResponse().addData(dataset);
	}

}

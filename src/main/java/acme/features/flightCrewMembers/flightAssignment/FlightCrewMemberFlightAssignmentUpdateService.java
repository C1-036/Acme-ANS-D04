
package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.AssignmentStatus;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.entities.flightCrewMembers.FlightDuty;
import acme.entities.flights.Leg;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		FlightAssignment flightAssignment;
		boolean status;
		int assignmentId;
		int memberId;

		assignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		memberId = flightAssignment == null ? null : super.getRequest().getPrincipal().getActiveRealm().getId();
		status = flightAssignment != null && flightAssignment.getFlightCrewMember().getId() == memberId && flightAssignment.isDraftMode() && !flightAssignment.getFlightLeg().isDraftMode()
			&& !flightAssignment.getFlightLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {

		FlightAssignment original = this.repository.findFlightAssignmentById(assignment.getId());
		assignment.setFlightCrewMember(original.getFlightCrewMember());

		super.bindObject(assignment, "duty", "status", "remarks", "flightLeg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Leg leg = assignment.getFlightLeg();
		boolean isPublished = !leg.isDraftMode();
		super.state(isPublished, "flightLeg", "acme.validation.flight-crew-member.assignment.form.error.leg-not-published", assignment);
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assert assignment != null;
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		SelectChoices flightCrewMemberChoice;
		Collection<FlightCrewMembers> flightCrewMembers;

		dutyChoice = SelectChoices.from(FlightDuty.class, assignment.getDuty());
		currentStatusChoice = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		legs = this.repository.findAllLegs();
		legChoice = SelectChoices.from(legs, "flightNumber", assignment.getFlightLeg());

		flightCrewMembers = this.repository.findAllFlightCrewMembers();
		flightCrewMemberChoice = SelectChoices.from(flightCrewMembers, "id", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "flightCrewMember", "flightLeg");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("flightCrewMemberChoice", flightCrewMemberChoice);

		super.getResponse().addData(dataset);
	}
}

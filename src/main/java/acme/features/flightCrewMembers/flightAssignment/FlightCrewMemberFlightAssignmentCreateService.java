
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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(true);
		flightAssignment.setStatus(AssignmentStatus.PENDING);
		super.getBuffer().addData(flightAssignment);

	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "status", "remarks", "flightLeg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		super.state(flightAssignment.getStatus() != AssignmentStatus.CANCELLED, "status", "acme.validation.flight-assignment.status-cancelled-not-allowed");
		super.state(flightAssignment.getStatus() != AssignmentStatus.CONFIRMED, "status", "acme.validation.flight-assignment.status-confirmed-not-allowed");

		Leg leg = flightAssignment.getFlightLeg();
		super.state(leg != null, "flightLeg", "acme.validation.flight-assignment.leg.required");

		if (leg != null) {
			boolean isPublished = !leg.isDraftMode();
			super.state(isPublished, "flightLeg", "acme.validation.flight-crew-member.assignment.form.error.leg-not-published", flightAssignment);

			int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			FlightCrewMembers member = this.repository.findFlightCrewMemberById(memberId);

			/*
			 * Airline legAirline = leg.getFlight().getAirline();
			 * Airline memberAirline = member.getAirline();
			 * 
			 * boolean sameAirline = legAirline.getId() == memberAirline.getId();
			 * super.state(sameAirline, "flightLeg", "acme.validation.flight-crew-member.assignment.form.error.different-airline", flightAssignment);
			 */
		}
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;
		this.repository.save(flightAssignment);
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

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "flightLeg");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);

		dataset.put("flightCrewMember.identity.fullName", assignment.getFlightCrewMember().getIdentity().getFullName());
		super.getResponse().addData(dataset);
	}

}

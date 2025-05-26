
package acme.features.flightCrewMembers.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.AssignmentStatus;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.entities.flightCrewMembers.FlightDuty;
import acme.entities.flights.Leg;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------

	//	@Override
	//	public void authorise() {
	//
	//		boolean isAuthorised = false;
	//
	//		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMembers.class))
	//
	//			// Only is allowed to update a flight assignment if the creator is associated.
	//			// A flight assignment cannot be updated if is published, only in draft mode are allowed.
	//			if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null) {
	//
	//				Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);
	//
	//				if (flightAssignmentId != null) {
	//
	//					FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
	//					FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
	//
	//					// Only is allowed to update a flight assignment if the leg selected is between the options shown.
	//					Collection<Leg> legs = this.repository.findAllLegsByAirlineId(flightCrewMember.getAirline().getId());
	//					Leg legSelected = super.getRequest().getData("flightLeg", Leg.class);
	//
	//					isAuthorised = flightAssignment != null && flightAssignment.isDraftMode() && flightAssignment.getFlightCrewMember().equals(flightCrewMember) && (legSelected == null || legs.contains(legSelected));
	//
	//				}
	//
	//			}
	//
	//		super.getResponse().setAuthorised(isAuthorised);
	//	}


	@Override
	public void authorise() {
		boolean status = false;
		boolean statusLeg = true;
		int flightAssignmentId;
		FlightAssignment flightAssignment;
		boolean isDraft;
		boolean isFlightCrewMembers;
		int legId;
		Leg leg;

		if (super.getRequest().hasData("id", int.class)) {
			flightAssignmentId = super.getRequest().getData("id", int.class);
			flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

			if (flightAssignment != null) {
				FlightCrewMembers flightCrewMembers = flightAssignment.getFlightCrewMember();
				isDraft = flightAssignment.isDraftMode();
				isFlightCrewMembers = super.getRequest().getPrincipal().hasRealm(flightCrewMembers);

				status = isDraft && isFlightCrewMembers;
			}
		}

		if (super.getRequest().hasData("flightLeg", int.class)) {
			legId = super.getRequest().getData("flightLeg", int.class);
			leg = this.repository.findLegById(legId);

			if (leg == null && legId != 0)
				statusLeg = false;
		}

		super.getResponse().setAuthorised(status && statusLeg);
	}

	@Override
	public void load() {
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		super.bindObject(flightAssignment, "duty", "status", "remarks", "flightLeg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		//flightAssignment.setMoment(MomentHelper.getCurrentMoment());
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "flightCrewMember", "flightLeg");

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		// Duty choices
		SelectChoices dutyChoices = SelectChoices.from(FlightDuty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		// Leg choices
		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsByAirlineId(flightAssignment.getFlightCrewMember().getAirline().getId()), "flightNumber", flightAssignment.getFlightLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("flightLeg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}

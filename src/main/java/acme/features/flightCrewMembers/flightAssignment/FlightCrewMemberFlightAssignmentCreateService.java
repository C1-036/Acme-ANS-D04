
package acme.features.flightCrewMembers.flightAssignment;

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

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------

	//	@Override
	//	public void authorise() {
	//
	//		boolean isAuthorised = false;
	//
	//		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMembers.class)) {
	//
	//			if (super.getRequest().getMethod().equals("GET"))
	//				isAuthorised = true;
	//
	//			// Only is allowed to create a flight assignment if post method include a valid flight assignment.
	//			if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null) {
	//
	//				FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
	//
	//				// Only is allowed to create a flight assignment if the leg selected is between the options shown.
	//				Collection<Leg> legs = this.repository.findAllLegsByAirlineId(flightCrewMember.getAirline().getId());
	//				Leg legSelected = super.getRequest().getData("flightLeg", Leg.class);
	//
	//				isAuthorised = (legSelected == null || legs.contains(legSelected)) && super.getRequest().getData("id", Integer.class).equals(0);
	//
	//			}
	//
	//		}
	//
	//		super.getResponse().setAuthorised(isAuthorised);
	//	}


	@Override
	public void authorise() {
		boolean status = true;
		int legId;
		Leg leg;

		if (super.getRequest().hasData("flightLeg", int.class)) {
			legId = super.getRequest().getData("flightLeg", int.class);
			leg = this.repository.findLegById(legId);

			if (leg == null && legId != 0)
				status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();

		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(true);

		FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
		flightAssignment.setFlightCrewMember(flightCrewMember);

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
		assert flightAssignment != null;

		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "flightCrewMember", "flightLeg");

		FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

		dataset.put("flightCrewMember", flightCrewMember.getIdentity().getFullName());

		// Duty choices
		SelectChoices dutyChoices = SelectChoices.from(FlightDuty.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		// Leg choices
		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsByAirlineId(flightCrewMember.getAirline().getId()), "flightNumber", flightAssignment.getFlightLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("flightLeg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}

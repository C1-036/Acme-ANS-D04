
package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberFlightAssignmentListLegsCompletedService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMembers.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
		Collection<FlightAssignment> completedFlightAssignments = this.repository.findAllCompletedFlightAssignments(MomentHelper.getCurrentMoment(), flightCrewMember.getId());

		super.getBuffer().addData(completedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignment) {
		assert completedFlightAssignment != null;

		Dataset dataset = super.unbindObject(completedFlightAssignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "flightLeg");

		dataset.put("flightLeg", completedFlightAssignment.getFlightLeg().getFlightNumber());

		super.addPayload(dataset, completedFlightAssignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "id");

		super.getResponse().addData(dataset);
	}

}


package acme.features.flightCrewMembers.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberFlightAssignmentListLegsPlannedService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//Collection<FlightAssignment> assignments = this.repository.assignmentsWithPlannedLegs(memberId);
		Collection<FlightAssignment> allAssignments = this.repository.findAllAssignmentsByMemberId(memberId);

		Date now = MomentHelper.getCurrentMoment();
		Collection<FlightAssignment> plannedAssignments = allAssignments.stream().filter(fa -> fa.getFlightLeg().getScheduledDeparture().after(now)).collect(Collectors.toList());
		super.getBuffer().addData(plannedAssignments);
		//super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status");
		super.addPayload(dataset, assignment, "remarks", "draftMode", "flightCrewMember.identity.fullName", "flightLeg.status");

		super.getResponse().addData(dataset);
	}

}

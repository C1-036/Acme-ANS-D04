
package acme.features.flightCrewMembers.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		ActivityLog log;
		int logId;
		int memberId;
		boolean status;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = log != null && log.getFlightAssignment().getFlightCrewMember().getId() == memberId && log.getFlightAssignment().getFlightLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "draftMode");
		FlightAssignment assignment = log.getFlightAssignment();
		String assignmentDescription = String.format("Flight %s - Duty: %s", assignment.getFlightLeg().getFlightNumber(), assignment.getDuty());
		dataset.put("flightAssignmentDescription", assignmentDescription);

		super.getResponse().addData(dataset);
	}
}


package acme.features.flightCrewMembers.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMembers.class))

			// Only is allowed to show an activity log if the creator is the flight crew member associated to the flight assignment.
			if (super.getRequest().getMethod().equals("GET") && super.getRequest().hasData("id")) {

				Integer activityLogId = super.getRequest().getData("id", Integer.class);

				if (activityLogId != null) {
					ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
					FlightAssignment flightAssignment = activityLog.getFlightAssignment();
					FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

					isAuthorised = activityLog != null && flightAssignment != null && !flightAssignment.isDraftMode() && activityLog.getFlightAssignment().getFlightCrewMember().equals(flightCrewMember);
				}

			}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		assert activityLog != null;

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity", "draftMode");

		// Show create if the assignment is completed
		// if (activityLog.getFlightAssignment().getFlightLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
		//	super.getResponse().addGlobal("showAction", true);

		boolean draftModeFlightAssignment = this.repository.findFlightAssignmentById(activityLog.getFlightAssignment().getId()).isDraftMode();
		dataset.put("draftModeFlightAssignment", draftModeFlightAssignment);

		super.getResponse().addData(dataset);
	}
}

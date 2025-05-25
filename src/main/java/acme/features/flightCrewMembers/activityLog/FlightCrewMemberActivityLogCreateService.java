
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
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMembers.class)) {
			if (super.getRequest().getMethod().equals("GET"))
				isAuthorised = true;

			// Only is allowed to create an activity log if the creator is the flight crew member associated to the flight assignment.
			// An activity log cannot be created if the assignment is planned, only complete are allowed.
			if (super.getRequest().getMethod().equals("POST") && super.getRequest().hasData("assignmentId") && super.getRequest().getData("id", Integer.class).equals(0)) {

				Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);

				if (assignmentId != null) {
					FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
					FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

					isAuthorised = flightAssignment != null && !flightAssignment.isDraftMode() && flightAssignment.getFlightCrewMember().equals(flightCrewMember);
				}

			}

		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();

		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		activityLog.setFlightAssignment(flightAssignment);

		activityLog.setDraftMode(true);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		assert activityLog != null;

		super.bindObject(activityLog, "incidentType", "description", "severity");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		assert activityLog != null;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		assert activityLog != null;

		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		assert activityLog != null;

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity", "draftMode");

		// Show create if the assignment is completed
		// super.getResponse().addGlobal("showAction", true);

		dataset.put("assignmentId", super.getRequest().getData("assignmentId", int.class));
		super.getResponse().addData(dataset);
	}

}

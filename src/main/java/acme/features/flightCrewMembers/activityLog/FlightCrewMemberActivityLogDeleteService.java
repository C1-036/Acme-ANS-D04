/*
 * package acme.features.flightCrewMembers.activityLog;
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * 
 * import acme.client.components.models.Dataset;
 * import acme.client.services.AbstractGuiService;
 * import acme.client.services.GuiService;
 * import acme.entities.flightCrewMembers.ActivityLog;
 * import acme.entities.flightCrewMembers.FlightAssignment;
 * import acme.realms.FlightCrewMembers;
 * 
 * @GuiService
 * public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {
 * 
 * @Autowired
 * private FlightCrewMemberActivityLogRepository repository;
 * 
 * // AbstractGuiService interface -------------------------------------------
 * 
 * 
 * @Override
 * public void authorise() {
 * 
 * boolean isAuthorised = false;
 * 
 * if (super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMembers.class))
 * 
 * // Only is allowed to delete an activity log if the creator is the flight crew member associated to the flight assignment.
 * // An activity log cannot be deleted if the activity log is published, only in draft mode are allowed.
 * if (super.getRequest().getData("id", Integer.class) != null) {
 * 
 * Integer activityLogId = super.getRequest().getData("id", Integer.class);
 * ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
 * 
 * if (activityLog != null) {
 * FlightAssignment flightAssignment = activityLog.getFlightAssignment();
 * FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
 * 
 * isAuthorised = flightAssignment != null && !flightAssignment.isDraftMode() && activityLog.getFlightAssignment().getFlightCrewMember().equals(flightCrewMember);
 * }
 * 
 * }
 * 
 * super.getResponse().setAuthorised(isAuthorised);
 * }
 * 
 * @Override
 * public void load() {
 * int id = super.getRequest().getData("id", int.class);
 * ActivityLog activityLog = this.repository.findActivityLogById(id);
 * 
 * super.getBuffer().addData(activityLog);
 * }
 * 
 * @Override
 * public void bind(final ActivityLog activityLog) {
 * assert activityLog != null;
 * }
 * 
 * @Override
 * public void validate(final ActivityLog activityLog) {
 * assert activityLog != null;
 * }
 * 
 * @Override
 * public void perform(final ActivityLog activityLog) {
 * assert activityLog != null;
 * 
 * this.repository.delete(activityLog);
 * }
 * 
 * @Override
 * public void unbind(final ActivityLog activityLog) {
 * Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity", "draftMode");
 * 
 * // Show create if the assignment is completed
 * // if (activityLog.getFlightAssignment().getFlightLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
 * // super.getResponse().addGlobal("showAction", true);
 * 
 * boolean draftModeFlightAssignment = this.repository.findFlightAssignmentById(activityLog.getFlightAssignment().getId()).isDraftMode();
 * dataset.put("draftModeFlightAssignment", draftModeFlightAssignment);
 * 
 * super.getResponse().addData(dataset);
 * }
 * 
 * }
 */

package acme.features.flightCrewMembers.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		FlightCrewMembers member;
		ActivityLog log;

		if (super.getRequest().getMethod().equals("POST")) {
			logId = super.getRequest().getData("id", int.class);
			log = this.repository.findActivityLogById(logId);
			member = log == null ? null : log.getFlightAssignment().getFlightCrewMember();
			status = log != null && log.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int logId;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "incidentType", "description", "severity");

	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
	}

	@Override
	public void unbind(final ActivityLog log) {
		;
	}

}

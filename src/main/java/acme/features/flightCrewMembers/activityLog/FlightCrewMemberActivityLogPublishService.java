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
 * public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {
 * 
 * // Internal state ---------------------------------------------------------
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
 * // Only is allowed to publish an activity log if the creator is the flight crew member associated to the flight assignment.
 * // An activity log cannot be published if:
 * // - Activity log should be in draft mode and not published.
 * // - Assignment should be in published mode and not in draft mode.
 * if (super.getRequest().getData("id", Integer.class) != null) {
 * 
 * Integer activityLogId = super.getRequest().getData("id", Integer.class);
 * ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
 * 
 * if (activityLog != null) {
 * FlightAssignment flightAssignment = activityLog.getFlightAssignment();
 * FlightCrewMembers flightCrewMember = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
 * 
 * isAuthorised = flightAssignment != null && activityLog.isDraftMode() && !flightAssignment.isDraftMode() && activityLog.getFlightAssignment().getFlightCrewMember().equals(flightCrewMember);
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
 * super.bindObject(activityLog, "incidentType", "description", "severity");
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
 * activityLog.setDraftMode(false);
 * this.repository.save(activityLog);
 * }
 * 
 * @Override
 * public void unbind(final ActivityLog activityLog) {
 * assert activityLog != null;
 * 
 * Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity", "flightAssignment", "draftMode");
 * 
 * // Show create if the assignment is completed
 * // if (activityLog.getFlightAssignment().getFlightLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
 * // super.getResponse().addGlobal("showAction", true);
 * 
 * super.getResponse().addData(dataset);
 * }
 * 
 * }
 */

/*
 * TechnicianTaskCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.flightCrewMembers.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		Integer activityLogId;
		ActivityLog activityLog;
		FlightCrewMembers flightCrewMembers;

		if (super.getRequest().hasData("id", Integer.class)) {
			activityLogId = super.getRequest().getData("id", Integer.class);
			if (activityLogId != null) {
				activityLog = this.repository.findActivityLogById(activityLogId);
				if (activityLog != null) {
					flightCrewMembers = activityLog.getFlightAssignment().getFlightCrewMember();
					status = activityLog.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flightCrewMembers);
				}
			}
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int activityLogId;
		ActivityLog activityLog;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {

		FlightCrewMembers flightCrewMembers = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(activityLog, "incidentType", "description", "severity");

		activityLog.getFlightAssignment().getFlightCrewMember().equals(flightCrewMembers);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {

		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "incidentType", "description", "severity", "flightAssignment", "draftMode");
		dataset.put("flightCrewMembers", activityLog.getFlightAssignment().getFlightCrewMember().getIdentity().getFullName());

		super.getResponse().addData(dataset);
	}

}

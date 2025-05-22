
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
		FlightAssignment assignment;
		int masterId;
		int memberId;
		boolean status;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = assignment != null && !assignment.isDraftMode() && assignment.getFlightCrewMember().getId() == memberId && assignment.getFlightLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int masterId;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);

		log = new ActivityLog();
		log.setFlightAssignment(assignment);
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setDraftMode(true);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registrationMoment", "incidentType", "description", "severity");

		int masterId = super.getRequest().getData("masterId", int.class);
		FlightAssignment assignment = this.repository.findFlightAssignmentById(masterId);
		log.setFlightAssignment(assignment);
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity");
		FlightAssignment assignment = log.getFlightAssignment();
		String assignmentDescription = String.format("Flight %s - Duty: %s", assignment.getFlightLeg().getFlightNumber(), assignment.getDuty());
		dataset.put("flightAssignmentDescription", assignmentDescription);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().addData(dataset);
	}
}

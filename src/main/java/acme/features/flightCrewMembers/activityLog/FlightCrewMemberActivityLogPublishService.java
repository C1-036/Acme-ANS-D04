
package acme.features.flightCrewMembers.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		ActivityLog log;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);

		status = log != null && log.isDraftMode() && log.getFlightAssignment() != null && !log.getFlightAssignment().isDraftMode();

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
	public void bind(final ActivityLog log) {
		super.bindObject(log, "incidentType", "description", "severity");
		ActivityLog original = this.repository.findActivityLogById(log.getId());
		log.setFlightAssignment(original.getFlightAssignment());
		log.setRegistrationMoment(original.getRegistrationMoment());

	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
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


package acme.features.flightCrewMembers.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightCrewMembers.ActivityLog;
import acme.realms.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightCrewMembers member = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

		Collection<ActivityLog> logs = this.repository.findAllLogsByFlightCrewMemberId(member.getId());

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "incidentType", "severity", "registrationMoment");
		super.addPayload(dataset, log, "description");
		super.getResponse().addData(dataset);

	}

}


package acme.features.assistanceagent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.TrackingLog;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentTrackingLogList extends AbstractGuiService<AssistanceAgents, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int assistanceAgentId;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		trackingLogs = this.repository.findTrackingLogsByAssistanceAgentId(assistanceAgentId);

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails");
		super.getResponse().addData(dataset);
	}
}

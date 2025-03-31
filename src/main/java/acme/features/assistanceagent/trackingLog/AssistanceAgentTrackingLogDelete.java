
package acme.features.assistanceagent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.TrackingLog;
import acme.entities.assistanceagents.TrackingLogState;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentTrackingLogDelete extends AbstractGuiService<AssistanceAgents, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(id);
		boolean canDelete = trackingLog != null && trackingLog.getAccepted() == TrackingLogState.PENDING;

		super.getResponse().setAuthorised(canDelete);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		super.state(trackingLog.getAccepted() == TrackingLogState.PENDING, "*", "acme.validation.trackinglog.delete-published");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.delete(trackingLog);
	}
}

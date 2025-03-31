
package acme.features.assistanceagent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.TrackingLog;
import acme.entities.assistanceagents.TrackingLogState;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentTrackingLogUpdate extends AbstractGuiService<AssistanceAgents, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(id);
		boolean canEdit = trackingLog != null && trackingLog.getAccepted() == TrackingLogState.PENDING;

		super.getResponse().setAuthorised(canEdit);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDetails");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		super.state(trackingLog.getAccepted() == TrackingLogState.PENDING, "*", "acme.validation.trackinglog.update-published");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDetails", "accepted");
		super.getResponse().addData(dataset);
	}
}

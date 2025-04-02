
package acme.features.assistanceagents.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Tracking;
import acme.entities.assistanceagents.TrackingState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdate extends AbstractGuiService<AssistanceAgent, Tracking> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Tracking trackingLog = this.repository.findTrackingLogById(id);
		boolean canEdit = trackingLog != null && trackingLog.getAccepted() == TrackingState.PENDING;

		super.getResponse().setAuthorised(canEdit);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Tracking trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final Tracking trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDetails");
	}

	@Override
	public void validate(final Tracking trackingLog) {
		super.state(trackingLog.getAccepted() == TrackingState.PENDING, "*", "acme.validation.trackinglog.update-published");
	}

	@Override
	public void perform(final Tracking trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final Tracking trackingLog) {
		Dataset dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDetails", "accepted");
		super.getResponse().addData(dataset);
	}
}


package acme.features.assistanceagents.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Tracking;
import acme.entities.assistanceagents.TrackingState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDelete extends AbstractGuiService<AssistanceAgent, Tracking> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Tracking trackingLog = this.repository.findTrackingLogById(id);
		boolean canDelete = trackingLog != null && trackingLog.getAccepted() == TrackingState.PENDING;

		super.getResponse().setAuthorised(canDelete);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Tracking trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void validate(final Tracking trackingLog) {
		super.state(trackingLog.getAccepted() == TrackingState.PENDING, "*", "acme.validation.trackinglog.delete-published");
	}

	@Override
	public void perform(final Tracking trackingLog) {
		this.repository.delete(trackingLog);
	}
}

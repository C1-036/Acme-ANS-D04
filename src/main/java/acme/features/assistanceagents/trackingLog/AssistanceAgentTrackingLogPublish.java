
package acme.features.assistanceagents.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.ClaimState;
import acme.entities.assistanceagents.TrackingLog;
import acme.entities.assistanceagents.TrackingLogState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublish extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(id);
		boolean canPublish = trackingLog != null && trackingLog.getAccepted() == TrackingLogState.PENDING;

		super.getResponse().setAuthorised(canPublish);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		Claim claim = trackingLog.getClaim();
		boolean claimPublished = claim.getAccepted() != ClaimState.PENDING;

		super.state(claimPublished, "*", "acme.validation.trackinglog.publish-claim-not-published");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setAccepted(TrackingLogState.ACCEPTED);
		this.repository.save(trackingLog);
	}
}

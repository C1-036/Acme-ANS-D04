
package acme.features.assistanceagents.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.ClaimState;
import acme.entities.assistanceagents.Tracking;
import acme.entities.assistanceagents.TrackingState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublish extends AbstractGuiService<AssistanceAgent, Tracking> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Tracking trackingLog = this.repository.findTrackingLogById(id);
		boolean canPublish = trackingLog != null && trackingLog.getAccepted() == TrackingState.PENDING;

		super.getResponse().setAuthorised(canPublish);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Tracking trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void validate(final Tracking trackingLog) {
		Claim claim = trackingLog.getClaim();
		boolean claimPublished = claim.getAccepted() != ClaimState.PENDING;

		super.state(claimPublished, "*", "acme.validation.trackinglog.publish-claim-not-published");
	}

	@Override
	public void perform(final Tracking trackingLog) {
		trackingLog.setAccepted(TrackingState.ACCEPTED);
		this.repository.save(trackingLog);
	}
}

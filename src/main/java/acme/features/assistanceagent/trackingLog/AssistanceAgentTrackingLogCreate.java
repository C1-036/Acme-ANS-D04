
package acme.features.assistanceagent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.ClaimState;
import acme.entities.assistanceagents.TrackingLog;
import acme.entities.assistanceagents.TrackingLogState;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentTrackingLogCreate extends AbstractGuiService<AssistanceAgents, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		status = claim != null && claim.getAccepted() == ClaimState.ACCEPTED;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog = new TrackingLog();
		int claimId = super.getRequest().getData("claimId", int.class);
		Claim claim = this.repository.findClaimById(claimId);

		trackingLog.setClaim(claim);
		trackingLog.setResolutionPercentage(0.0);
		trackingLog.setAccepted(TrackingLogState.PENDING);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean canCreateNewLog;
		canCreateNewLog = this.repository.existsTrackingLogWithFullResolution(trackingLog.getClaim().getId());

		if (canCreateNewLog && trackingLog.getResolutionPercentage() < 100)
			super.state(false, "resolutionPercentage", "acme.validation.trackinglog.must-be-100-percent");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

}

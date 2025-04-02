
package acme.features.assistanceagents.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.Tracking;
import acme.entities.assistanceagents.TrackingState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreate extends AbstractGuiService<AssistanceAgent, Tracking> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int claimId = super.getRequest().getData("claimId", int.class);
		Claim claim = this.repository.findClaimById(claimId);

		boolean isCustomerDissatisfied = this.repository.isCustomerDissatisfied(claimId);
		boolean hasFullResolutionLog = this.repository.existsTrackingLogWithFullResolution(claimId);

		if (hasFullResolutionLog && !isCustomerDissatisfied) {
			super.getResponse().setAuthorised(false);
			return;
		}

		Tracking trackingLog = new Tracking();
		trackingLog.setClaim(claim);
		trackingLog.setResolutionPercentage(0.0);
		trackingLog.setAccepted(TrackingState.PENDING);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final Tracking trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails");
	}

	@Override
	public void validate(final Tracking trackingLog) {
		boolean canCreateNewLog;
		canCreateNewLog = this.repository.existsTrackingLogWithFullResolution(trackingLog.getClaim().getId());

		if (canCreateNewLog && trackingLog.getResolutionPercentage() < 100)
			super.state(false, "resolutionPercentage", "acme.validation.trackinglog.must-be-100-percent");
	}

	@Override
	public void perform(final Tracking trackingLog) {
		this.repository.save(trackingLog);
	}

}


package acme.features.assistanceagents.tracking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.Tracking;
import acme.entities.assistanceagents.TrackingState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreate extends AbstractGuiService<AssistanceAgent, Tracking> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		int currentAssistanceAgentId;
		Claim claim;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && claim.getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int claimId;
		Tracking trackingLog;
		Claim claim;
		Date currentMoment;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		currentMoment = MomentHelper.getCurrentMoment();

		trackingLog = new Tracking();
		trackingLog.setClaim(claim);
		trackingLog.setLastUpdateMoment(currentMoment);
		trackingLog.setDraftMode(true);

		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final Tracking trackingLog) {
		super.bindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails");
	}

	@Override
	public void validate(final Tracking trackingLog) {
		boolean allTrackingLogsPublished;
		int claimId;
		Collection<Tracking> trackingLogs;

		allTrackingLogsPublished = true;
		claimId = trackingLog.getClaim().getId();
		trackingLogs = this.repository.findTrackingLogsByClaimId(claimId);

		for (Tracking element : trackingLogs)
			if (element.isDraftMode())
				allTrackingLogsPublished = false;

		if (!allTrackingLogsPublished)
			super.state(allTrackingLogsPublished, "*", "assistance-agent.tracking-log.form.error.allTrackingLogsPublished");

	}

	@Override
	public void perform(final Tracking trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final Tracking trackingLog) {
		Dataset dataset;
		SelectChoices accepteds;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails");
		accepteds = SelectChoices.from(TrackingState.class, trackingLog.getAccepted());
		dataset.put("accepteds", accepteds);
		dataset.put("claimId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);
	}

}

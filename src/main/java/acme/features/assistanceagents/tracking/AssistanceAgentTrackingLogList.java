
package acme.features.assistanceagents.tracking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.Tracking;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogList extends AbstractGuiService<AssistanceAgent, Tracking> {

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
		Collection<Tracking> trackingLogs;

		claimId = super.getRequest().getData("claimId", int.class);

		trackingLogs = this.repository.findTrackingLogsByClaimId(claimId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final Tracking trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<Tracking> objects) {
		int claimId;

		claimId = super.getRequest().getData("claimId", int.class);

		super.getResponse().addGlobal("claimId", claimId);
	}
}

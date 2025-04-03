
package acme.features.assistanceagents.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Tracking;
import acme.entities.assistanceagents.TrackingState;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShow extends AbstractGuiService<AssistanceAgent, Tracking> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		int currentAssistanceAgentId;
		Tracking trackingLog;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && trackingLog.getClaim().getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int trackingLogId;
		Tracking trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final Tracking trackingLog) {
		Dataset dataset;
		SelectChoices accepteds;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "accepted", "resolutionDetails", "draftMode");
		accepteds = SelectChoices.from(TrackingState.class, trackingLog.getAccepted());
		dataset.put("accepteds", accepteds);

		super.getResponse().addData(dataset);

	}
}

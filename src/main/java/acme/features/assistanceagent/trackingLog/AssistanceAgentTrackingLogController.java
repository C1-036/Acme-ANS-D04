
package acme.features.assistanceagent.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.assistanceagents.TrackingLog;
import acme.realms.AssistanceAgents;

@GuiController
public class AssistanceAgentTrackingLogController extends AbstractGuiController<AssistanceAgents, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogList		listService;

	@Autowired
	private AssistanceAgentTrackingLogShow		showService;

	@Autowired
	private AssistanceAgentTrackingLogCreate	createService;

	@Autowired
	private AssistanceAgentTrackingLogUpdate	updateService;

	@Autowired
	private AssistanceAgentTrackingLogDelete	deleteService;

	@Autowired
	private AssistanceAgentTrackingLogPublish	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}

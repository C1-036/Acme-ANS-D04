
package acme.features.assistanceagent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.assistanceagents.Claim;
import acme.realms.AssistanceAgents;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgents, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimList			listService;

	@Autowired
	private AssistanceAgentClaimShow			showService;

	@Autowired
	private AssistanceAgentClaimListCompleted	listCompleted;

	@Autowired
	private AssistanceAgentClaimShowCompleted	showCompleted;

	@Autowired
	private AssistanceAgentClaimListUndergoing	listUndergoing;

	@Autowired
	private AssistanceAgentClaimShowUndergoing	showUndergoing;

	@Autowired
	private AssistanceAgentClaimCreate			createClaim;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createClaim);

		super.addCustomCommand("listCompleted", "list", this.listCompleted);
		super.addCustomCommand("showCompleted", "show", this.showCompleted);
		super.addCustomCommand("listUndergoing", "list", this.listUndergoing);
		super.addCustomCommand("showUndergoing", "show", this.showUndergoing);
	}

}

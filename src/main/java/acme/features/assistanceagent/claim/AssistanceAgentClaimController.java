
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
		super.addBasicCommand("listCompleted", this.listCompleted);
		super.addBasicCommand("showCompleted", this.showCompleted);
		super.addBasicCommand("listUndergoing", this.listUndergoing);
		super.addBasicCommand("showUndergoing", this.showUndergoing);
		super.addBasicCommand("createClaim", this.createClaim);
	}

}

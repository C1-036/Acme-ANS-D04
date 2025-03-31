
package acme.features.assistanceagent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.ClaimState;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentClaimCreate extends AbstractGuiService<AssistanceAgents, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AssistanceAgents agent;
		agent = (AssistanceAgents) super.getRequest().getPrincipal().getActiveRealm();

		Claim claim = new Claim();
		claim.setDraftMode(true);
		claim.setAssistanceAgent(agent);
		claim.setAccepted(ClaimState.PENDING);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
	}

	@Override
	public void validate(final Claim claim) {
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");

		super.getResponse().addData(dataset);
	}
}

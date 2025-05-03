
package acme.features.assistanceagents.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.ClaimState;
import acme.entities.assistanceagents.ClaimType;
import acme.entities.flights.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdate extends AbstractGuiService<AssistanceAgent, Claim> {
	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int assistanceAgentId;
		int claimId;
		AssistanceAgent assistanceAgent;
		Claim selectedClaim;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
		claimId = super.getRequest().getData("id", int.class);
		selectedClaim = this.repository.findClaimById(claimId);

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && selectedClaim.getAssistanceAgent().equals(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(claimId);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "passengerEmail", "description", "type", "accepted", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		Leg leg = claim.getLegs();

		super.state(leg != null, "leg", "assistanceAgent.claim.form.error.emptyLeg");

		if (leg != null)
			super.state(!leg.isDraftMode(), "leg", "assistanceAgent.claim.form.error.legNotPublished");
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices types;
		SelectChoices accepteds;
		SelectChoices legsChoices;

		Collection<Leg> legs;
		legs = this.repository.findAllLegsNotPublished();

		types = SelectChoices.from(ClaimType.class, claim.getType());
		accepteds = SelectChoices.from(ClaimState.class, claim.getAccepted());
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLegs());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "accepted");
		dataset.put("types", types);
		dataset.put("accepteds", accepteds);
		dataset.put("legs", legsChoices);
		dataset.put("leg", legsChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}

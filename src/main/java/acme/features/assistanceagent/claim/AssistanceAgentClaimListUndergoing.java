
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assistanceagents.Claim;
import acme.entities.flights.Leg;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentClaimListUndergoing extends AbstractGuiService<AssistanceAgents, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> undergoingClaims;
		int assistanceAgentId;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		undergoingClaims = this.repository.findUndergoingClaimsByAssistanceAgentId(assistanceAgentId);

		super.getBuffer().addData(undergoingClaims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Leg linkedLeg;

		linkedLeg = claim.getLeg();  //Relacion con leg????

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "accepted");

		if (linkedLeg != null) {
			dataset.put("legFlightNumber", linkedLeg.getFlightNumber());
			dataset.put("legDepartureAirport", linkedLeg.getDepartureAirport().getName());
			dataset.put("legArrivalAirport", linkedLeg.getArrivalAirport().getName());
			dataset.put("legScheduledDeparture", linkedLeg.getScheduledDeparture());
			dataset.put("legScheduledArrival", linkedLeg.getScheduledArrival());
			dataset.put("legStatus", linkedLeg.getStatus().toString());
		} else
			dataset.put("legFlightNumber", "No linked leg");

		super.getResponse().addData(dataset);
	}

}

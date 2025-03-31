
package acme.features.airlinemanager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.Indication;
import acme.entities.flights.Leg;
import acme.features.airlinemanager.leg.AirlineManagerLegRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);

		super.state(flight != null, "*", "acme.validation.airline-manager.flight.invalid-request");
		if (flight == null)
			return;

		AirlineManager current = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean isOwner = flight.getAirlinemanager().equals(current);
		super.state(isOwner, "*", "acme.validation.airline-manager.flight.not-owner");
		if (!isOwner)
			return;

		boolean isDraft = flight.isDraftMode();
		super.state(isDraft, "*", "acme.validation.airline-manager.flight.not-in-draft");

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		int flightId = flight.getId();

		Collection<Leg> legs = this.repository.findLegsByFlightId(flightId);
		super.state(!legs.isEmpty(), "*", "acme.validation.airline-manager.flight.no-legs");

		boolean allLegsPublished = legs.stream().allMatch(l -> !l.isDraftMode());
		super.state(allLegsPublished, "*", "acme.validation.airline-manager.flight.legs-not-published");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		SelectChoices choices;
		choices = SelectChoices.from(Indication.class, flight.getSelfTransfer());

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("selfTransfer", choices.getSelected().getKey());
		dataset.put("selfTransfers", choices);

		super.getResponse().addData(dataset);
	}

}

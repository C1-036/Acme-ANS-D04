
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
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);
		AirlineManager current = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean status = flight != null && flight.getAirlinemanager().equals(current) && flight.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);

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

		if (!super.getBuffer().getErrors().hasErrors("selfTransfer")) {
			boolean requiresSelfTransfer = this.repository.requiresSelfTransferBetweenConsecutiveLegs(flight.getId());

			if (flight.getSelfTransfer() == Indication.NOT_SELF_TRANSFER && requiresSelfTransfer)
				super.state(false, "selfTransfer", "acme.validation.airline-manager.flight.invalid-selfTransfer");
		}

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

		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("layovers", flight.getLayovers());

		dataset.put("id", flight.getId());
		super.getResponse().addData(dataset);
	}

}

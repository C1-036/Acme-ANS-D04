
package acme.features.airlinemanager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.Indication;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightCreateService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight;
		AirlineManager manager;

		manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDraftMode(true);
		flight.setAirlinemanager(manager);
		flight.setSelfTransfer(Indication.SELF_TRANSFER);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
	}

	@Override
	public void perform(final Flight flight) {
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

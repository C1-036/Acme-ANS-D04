
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
		boolean status;
		int flightId;
		Flight flight;
		AirlineManager airlinemanager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);
		airlinemanager = flight == null ? null : flight.getAirlinemanager();

		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(airlinemanager);
		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

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
		super.state(!legs.isEmpty(), "*", "acme.validation.flight.no-legs");

		boolean allLegsPublished = legs.stream().allMatch(l -> !l.isDraftMode());
		super.state(allLegsPublished, "*", "acme.validation.flight.legs-not-published");
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

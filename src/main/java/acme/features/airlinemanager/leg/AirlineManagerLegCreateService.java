
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;
import acme.entities.flights.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(flightId);

		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getAirlinemanager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		Flight flight;
		int flightId;

		flightId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(flightId);

		leg = new Leg();
		leg.setFlight(flight);
		leg.setStatus(LegStatus.ON_TIME);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId;
		int arrivalAirportId;
		int aircraftId;
		String statusValue;

		departureAirportId = Integer.valueOf(super.getRequest().getData("departureAirport", String.class));
		arrivalAirportId = Integer.valueOf(super.getRequest().getData("arrivalAirport", String.class));
		aircraftId = Integer.valueOf(super.getRequest().getData("aircraft", String.class));
		statusValue = super.getRequest().getData("status", String.class);

		Airport departure = this.repository.findAirportById(departureAirportId);
		Airport arrival = this.repository.findAirportById(arrivalAirportId);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		leg.setStatus(LegStatus.valueOf(statusValue));
		leg.setDepartureAirport(departure);
		leg.setArrivalAirport(arrival);
		leg.setAircraft(aircraft);
	}

	@Override
	public void validate(final Leg leg) {
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;
		Dataset dataset;
		SelectChoices statuses;
		Collection<Airport> airports = this.repository.findAllAirports();
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		statuses = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("status", statuses.getSelected().getKey());
		dataset.put("statuses", statuses);
		dataset.put("airports", airports);
		dataset.put("aircrafts", aircrafts);
		dataset.put("departureAirport", leg.getDepartureAirport().getId());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getId());
		dataset.put("aircraft", leg.getAircraft().getId());
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("draftMode", leg.getFlight().isDraftMode());

		super.getResponse().addData(dataset);
	}

}

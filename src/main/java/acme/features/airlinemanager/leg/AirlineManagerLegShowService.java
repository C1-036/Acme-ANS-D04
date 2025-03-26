/*
 * AuthenticatedAnnouncementShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

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
public class AirlineManagerLegShowService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		Flight flight;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		flight = leg == null ? null : leg.getFlight();

		status = leg != null && flight != null && super.getRequest().getPrincipal().hasRealm(flight.getAirlinemanager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);
		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		assert leg != null;
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		// Statuses
		SelectChoices statuses = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("status", statuses.getSelected().getKey());
		dataset.put("statuses", statuses);

		// Departure and arrival airports
		Collection<Airport> airports = this.repository.findAllAirports();
		SelectChoices departureChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		SelectChoices arrivalChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());

		dataset.put("departureAirport", departureChoices.getSelected().getKey());
		dataset.put("departureAirportChoices", departureChoices);
		dataset.put("arrivalAirport", arrivalChoices.getSelected().getKey());
		dataset.put("arrivalAirportChoices", arrivalChoices);

		// Aircrafts
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircraftChoices", aircraftChoices);

		// Duration (readonly)
		dataset.put("durationHours", leg.getDurationHours());

		// Flight info
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("draftMode", leg.getFlight().isDraftMode());

		super.getResponse().addData(dataset);
	}

}

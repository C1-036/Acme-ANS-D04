/*
 * 
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
import acme.entities.flights.Leg;
import acme.entities.flights.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegDeleteService extends AbstractGuiService<AirlineManager, Leg> {

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
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);

		super.state(leg != null, "*", "acme.validation.airline-manager.leg.invalid-request");
		if (leg == null)
			return;

		AirlineManager current = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean isOwner = leg.getFlight().getAirlinemanager().equals(current);
		super.state(isOwner, "*", "acme.validation.airline-manager.leg.not-owner");
		if (!isOwner)
			return;

		boolean flightInDraft = leg.getFlight().isDraftMode();
		boolean legInDraft = leg.isDraftMode();

		if (!flightInDraft)
			super.state(false, "*", "acme.validation.airline-manager.leg.flight-not-in-draft");

		if (!legInDraft)
			super.state(false, "*", "acme.validation.airline-manager.leg.leg-not-in-draft");

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId = super.getRequest().getData("departureAirport", int.class);
		int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		int aircraftId = super.getRequest().getData("aircraft", int.class);
		String statusValue = super.getRequest().getData("status", String.class);

		Airport departure = this.repository.findAirportById(departureAirportId);
		Airport arrival = this.repository.findAirportById(arrivalAirportId);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		leg.setDepartureAirport(departure);
		leg.setArrivalAirport(arrival);
		leg.setAircraft(aircraft);
		leg.setStatus(LegStatus.valueOf(statusValue));
	}

	@Override
	public void validate(final Leg leg) {

		if (!leg.getFlight().isDraftMode())
			super.state(false, "*", "acme.validation.airline-manager.leg.flight-not-in-draft");

		int flightId = leg.getFlight().getId();
		Collection<Leg> legs = this.repository.findLegsByFlightId(flightId);

		boolean isOnlyLeg = legs.size() == 1;
		super.state(!isOnlyLeg, "*", "acme.validation.airline-manager.leg.delete.only-one");

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices departureAirportChoices;
		SelectChoices arrivalAirportChoices;
		SelectChoices aircraftChoices;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		statuses = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("status", statuses.getSelected().getKey());
		dataset.put("statuses", statuses);

		Collection<Airport> airports = this.repository.findAllAirports();
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();

		departureAirportChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		arrivalAirportChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		if (leg.getDepartureAirport() != null)
			dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());

		if (leg.getArrivalAirport() != null)
			dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());

		if (leg.getAircraft() != null)
			dataset.put("aircraft", aircraftChoices.getSelected().getKey());

		dataset.put("departureAirportChoices", departureAirportChoices);
		dataset.put("arrivalAirportChoices", arrivalAirportChoices);
		dataset.put("aircraftChoices", aircraftChoices);
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("draftMode", leg.isDraftMode());

		super.getResponse().addData(dataset);
	}

}

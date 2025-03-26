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
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractService<Employer, Job> -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		AirlineManager airlineManager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		airlineManager = flight == null ? null : flight.getAirlinemanager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(airlineManager);

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
		int assignmentCount = this.repository.countAssignmentsByFlightId(flight.getId());

		if (assignmentCount > 0)
			super.state(false, "*", "acme.validation.flight.delete-has-assignments");
	}

	@Override
	public void perform(final Flight flight) {
		Collection<Leg> legs;

		legs = this.repository.findLegsByFlightId(flight.getId());
		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Indication.class, flight.getSelfTransfer());

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("selfTransfer", choices.getSelected().getKey());
		dataset.put("selfTransfers", choices);

		super.getResponse().addData(dataset);
	}

}

/*
 * AuthenticatedAirlineManagerCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.airlinemanager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.AirlineManager;

@Service
@GuiService
public class AuthenticatedAirlineManagerCreateService extends AbstractGuiService<Authenticated, AirlineManager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAirlineManagerRepository repository;

	// AbstractService<Authenticated, AirlineManager> ---------------------------


	@Override
	public void authorise() {
		boolean status = !this.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userAccountId = this.getRequest().getPrincipal().getAccountId();
		UserAccount userAccount = this.repository.findUserAccountById(userAccountId);

		AirlineManager manager = new AirlineManager();
		manager.setUserAccount(userAccount);

		super.getBuffer().addData(manager);
	}

	@Override
	public void bind(final AirlineManager manager) {
		super.bindObject(manager, "identifierNumber", "yearsOfExperience", "dateOfBirth", "picture");
		String airlineIdStr = (String) this.getRequest().getData().get("airlineId");
		int airlineId = Integer.parseInt(airlineIdStr);
		Airline a = this.repository.findAirlineById(airlineId);
		manager.setAirline(a);
	}

	@Override
	public void validate(final AirlineManager manager) {
	}

	@Override
	public void perform(final AirlineManager manager) {
		this.repository.save(manager);
	}

	@Override
	public void unbind(final AirlineManager manager) {
		Dataset dataset = super.unbindObject(manager, "identifierNumber", "yearsOfExperience", "dateOfBirth", "picture");
		Collection<Airline> list = this.repository.findAllAirlines();
		SelectChoices choices = SelectChoices.from(list, "name", manager.getAirline());
		dataset.put("airlineId", choices.getSelected().getKey());
		dataset.put("airlines", choices);
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if ("POST".equals(this.getRequest().getMethod()))
			PrincipalHelper.handleUpdate();
	}
}

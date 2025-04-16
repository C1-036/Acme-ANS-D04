
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRealm(customer));
	}

	@Override
	public void load() {
		Collection<Passenger> passenger;
		int passengerId;

		passengerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		passenger = this.repository.findAllPassengerByCustomer(passengerId);

		super.getBuffer().addData(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth");
		super.addPayload(dataset, passenger, "specialNeeds");
		super.getResponse().addData(dataset);
	}

}


package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Passenger passenger;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		status = passenger != null;

		super.getResponse().setAuthorised(status);

	}

}

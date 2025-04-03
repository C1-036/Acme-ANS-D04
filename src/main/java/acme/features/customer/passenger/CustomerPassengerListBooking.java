
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListBooking extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(Customer.class));
	}

	@Override
	public void load() {
		Collection<Passenger> passenger;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);

		passenger = this.repository.findAllPassengerByBooking(bookingId);

		super.getBuffer().addData(passenger);
		super.getResponse().addGlobal("bookingId", bookingId);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth");
		super.addPayload(dataset, passenger, "specialNeeds");
		super.getResponse().addData(dataset);

	}
}

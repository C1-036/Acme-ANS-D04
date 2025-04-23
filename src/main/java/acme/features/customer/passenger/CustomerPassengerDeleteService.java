
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.CustomerRepository;
import acme.entities.customers.Make;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository	repository;

	@Autowired
	private CustomerRepository			customerRepository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Passenger passenger;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerByPassengerId(masterId);
		customer = passenger == null ? null : passenger.getCustomer();

		status = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer) && passenger.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerByPassengerId(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {
		Collection<Make> make;

		make = this.repository.findAllMakeByPassenger(passenger.getId());
		this.repository.deleteAll(make);
		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		int customerId;
		Customer customer;
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.customerRepository.findById(customerId);
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
		dataset.put("customer", customer);
		super.getResponse().addData(dataset);
	}

}

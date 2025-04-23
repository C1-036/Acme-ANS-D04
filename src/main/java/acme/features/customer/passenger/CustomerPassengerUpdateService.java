
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.CustomerRepository;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository	repository;

	@Autowired
	private CustomerRepository			customerRepository;


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Passenger passenger;
		Customer customer;

		customerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerByPassengerId(customerId);
		customer = passenger == null ? null : passenger.getCustomer();
		status = passenger != null && super.getRequest().getPrincipal().hasRealm(customer) && passenger.isDraftMode();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerByPassengerId(id);

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
		this.repository.save(passenger);
	}
	@Override
	public void unbind(final Passenger passenger) {
		int customerId;
		Customer customer;
		Dataset dataset;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.customerRepository.findById(customerId);

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
		dataset.put("customer", customer);

		super.getResponse().addData(dataset);
	}

}

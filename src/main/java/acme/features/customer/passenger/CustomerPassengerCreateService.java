
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.CustomerRepository;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository	repository;

	@Autowired
	private CustomerRepository			customerRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId;
		Customer customer;
		Passenger passenger;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.customerRepository.findById(customerId);

		passenger = new Passenger();
		passenger.setDraftMode(true);
		passenger.setCustomer(customer);
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
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.customerRepository.findById(customerId);
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");
		dataset.put("customer", customer);
		super.getResponse().addData(dataset);

	}

}

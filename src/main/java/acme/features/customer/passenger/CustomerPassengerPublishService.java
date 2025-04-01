
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.CustomerRepository;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

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

		status = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
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

		assert passenger != null;

		if (!super.getBuffer().getErrors().hasErrors("booking")) {
			Collection<Booking> booking;

			booking = this.repository.findBookingByPassenger(passenger.getId());

			super.state(!booking.isEmpty(), "booking", "javax.validation.constraints.NotNull.message");
		}
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
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

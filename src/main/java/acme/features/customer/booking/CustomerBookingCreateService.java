
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.CustomerRepository;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository	repository;

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
		Booking booking;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.customerRepository.findById(customerId);

		booking = new Booking();
		booking.setDraftMode(true);
		booking.setCustomer(customer);
		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		int customerId;
		int flightId;
		Customer customer;
		Dataset dataset;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.customerRepository.findById(customerId);

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard");
		dataset.put("customer", customer);
		dataset.put("flight", flight);
		super.getResponse().addData(dataset);

	}

}

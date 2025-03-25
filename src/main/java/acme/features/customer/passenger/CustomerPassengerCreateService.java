
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.Make;
import acme.entities.customers.MakeRepository;
import acme.entities.customers.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository	repository;

	@Autowired
	private MakeRepository				makeRepository;

	@Autowired
	private CustomerBookingRepository	bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId;
		Customer customer;
		Passenger passenger;
		Make make;
		Booking booking;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		booking = this.bookingRepository.findABookingByCustomer(customerId);
		make = this.makeRepository.findByBookingId(booking.getId());

		passenger = new Passenger();
		passenger.setDraftMode(true);
		make.setPassenger(passenger);

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
		int passengerId;
		Dataset dataset;

		passengerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);

	}

}

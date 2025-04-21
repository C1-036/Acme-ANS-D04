
package acme.features.customer.make;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.Make;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerMakeDeleteService extends AbstractGuiService<Customer, Make> {

	@Autowired
	private CustomerMakeRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		customer = booking == null ? null : booking.getCustomer();
		status = booking != null && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Make make;
		Integer bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		make = new Make();
		make.setBooking(booking);
		super.getBuffer().addData(make);
		super.getResponse().addGlobal("bookingId", bookingId);

	}

	@Override
	public void bind(final Make make) {
		;
	}

	@Override
	public void validate(final Make make) {
		;
	}

	@Override
	public void perform(final Make make) {
		Passenger passenger = super.getRequest().getData("passenger", Passenger.class);
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		this.repository.delete(this.repository.findMakeByBookingAndPassenger(booking, passenger));

	}

	@Override
	public void unbind(final Make make) {
		Collection<Passenger> passenger;
		Dataset dataset;
		int bookingId;
		Booking booking;
		SelectChoices choices;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		passenger = this.repository.findValidPassengersToUnlink(booking);
		choices = SelectChoices.from(passenger, "fullName", make.getPassenger());

		dataset = super.unbindObject(make, "booking");
		dataset.put("bookingId", make.getBooking().getId());
		dataset.put("passenger", choices.getSelected().getKey());
		dataset.put("passengers", choices);
		dataset.put("tag", make.getBooking().getFlight().getTag());

		super.getResponse().addData(dataset);

	}

}

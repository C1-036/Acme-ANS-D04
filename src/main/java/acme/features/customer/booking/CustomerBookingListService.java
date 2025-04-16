
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRealm(customer));
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		bookings = this.repository.findAllBookingByCustomer(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		int bookingId = booking.getId();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard");
		super.addPayload(dataset, booking, "customer", "flight");

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("bookingId", bookingId);

	}

}

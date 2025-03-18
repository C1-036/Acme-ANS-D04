
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.customers.Booking;
import acme.realms.Customer;

@Service
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository	repository;

	Customer							customer;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		Date currentMoment;
		currentMoment = MomentHelper.getCurrentMoment();

		bookings = this.repository.findAllBookingByCustomer(this.customer);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard");
		super.getResponse().addData(dataset);

	}

}


package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		int masterId;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(masterId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard");

		dataset.put("customer", booking.getCustomer());
		dataset.put("flight", booking.getFlight());

		super.getResponse().addData(dataset);

	}

}


package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Booking;
import acme.entities.customers.Passenger;
import acme.entities.customers.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Booking booking;

		customerId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(customerId);

		Customer current = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = booking != null && booking.getCustomer().equals(current) && booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCard");

		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		assert booking != null;

		if (!super.getBuffer().getErrors().hasErrors("creditCard")) {
			String card = booking.getCreditCard();

			super.state(!card.isBlank(), "creditCard", "javax.validation.constraints.NotNull.message");
		}

		if (!super.getBuffer().getErrors().hasErrors("passenger")) {
			Collection<Passenger> passengers;

			passengers = this.repository.findAllPassengerBooking(booking.getId());

			super.state(!passengers.isEmpty(), "*", "acme.validation.customer.booking.no-associated-passenger"); //El mensaje esta mal,

		}
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		Collection<Flight> flights;
		SelectChoices choices2;

		flights = this.repository.findAllFlights();

		choices = SelectChoices.from(flights, "tag", booking.getFlight());
		choices2 = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCard", "customer", "flight", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("travelClass", choices2.getSelected().getKey());
		dataset.put("travelClasss", choices2);

		super.getResponse().addData(dataset);

	}

}

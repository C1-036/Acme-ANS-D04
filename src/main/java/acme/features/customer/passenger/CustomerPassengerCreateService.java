
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.customers.Make;
import acme.entities.customers.MakeRepository;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository	repository;

	@Autowired
	private MakeRepository				makeRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		//Customer customer;
		Passenger passenger;
		//Make make = (Make) super.getRequest().getData("make");
		Customer customer = (Customer) super.getRequest().getData("customer");
		//customer.getB

		//Booking booking = make.getBooking();
		Make make = new Make();
		//make.setBooking(booking);
		passenger = new Passenger();
		passenger.setDraftMode(true);
		make.getBooking();

		//make.setPassenger(passenger);
		//this.makeRepository.save(make);

		//super.getBuffer().addData(make);
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
		int passengerId;
		Dataset dataset;

		passengerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);

	}

}

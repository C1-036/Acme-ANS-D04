
package acme.features.customer.make;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.customers.Booking;
import acme.entities.customers.Make;
import acme.entities.customers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerMakeRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select p from Passenger p where p not in (select m.passenger from Make m where m.booking = :booking) and p.customer = :customer")
	Collection<Passenger> findValidPassengersToLink(Booking booking, Customer customer);

	@Query("select p from Passenger p where p in (select m.passenger from Make m where m.booking = :booking)")
	Collection<Passenger> findValidPassengersToUnlink(Booking booking);

	@Query("select m from Make m where m.booking = :booking")
	Collection<Make> findMakeByBooking(Booking booking);

	@Query("select m from Make m where m.booking = :booking and m.passenger = :passenger")
	Make findMakeByBookingAndPassenger(Booking booking, Passenger passenger);
}


package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.customers.Booking;
import acme.entities.customers.Passenger;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :id")
	Collection<Booking> findAllBookingByCustomer(int id);

	@Query("select b FROM Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select m.passenger from Make m where m.booking.id = :bookingId")
	Collection<Passenger> findAllPassengerBooking(int bookingId);

}

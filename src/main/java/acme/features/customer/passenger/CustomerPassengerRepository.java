
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.customers.Booking;
import acme.entities.customers.Make;
import acme.entities.customers.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :id")
	Collection<Passenger> findAllPassengerByCustomer(int id);

	@Query("SELECT p FROM Passenger p WHERE p.id = :passengerId")
	Passenger findPassengerByPassengerId(int passengerId);

	@Query("SELECT m.passenger FROM Make m WHERE m.booking.id = :bookingId")
	Collection<Passenger> findAllPassengerByBooking(int bookingId);

	@Query("Select m.passenger FROM Make m where m.booking.id = :bookingId")
	Passenger findPassengerByBookingId(int bookingId);

	@Query("SELECT m.booking FROM Make m WHERE m.passenger.id = :passengerId")
	Collection<Booking> findBookingByPassenger(int passengerId);

	@Query("Select m from Make m where m.passenger.id = :passengerId")
	Collection<Make> findAllMakeByPassenger(int passengerId);
}

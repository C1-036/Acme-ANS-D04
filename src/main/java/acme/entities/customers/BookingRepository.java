
package acme.entities.customers;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b.creditCard FROM Booking b WHERE b.id = :bookingId")
	String findCreditCardByBookingId(int bookingId);

	@Query("SELECT b FROM Booking b WHERE b.creditCard = :creditCard")
	Booking findByCreditCard(String creditCard);

}

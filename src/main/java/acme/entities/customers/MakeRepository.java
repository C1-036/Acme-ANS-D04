
package acme.entities.customers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface MakeRepository extends AbstractRepository {

	//@Query("SELECT m FROM Make m WHERE m.booking.id = :bookingId")
	@Query("SELECT m FROM Make m WHERE m.booking.id = :bookingId ORDER BY m.id ASC")
	Make findByBookingId(int bookingId);

}

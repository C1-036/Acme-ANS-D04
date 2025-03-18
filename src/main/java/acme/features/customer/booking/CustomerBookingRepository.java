
package acme.features.customer.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.customers.Booking;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select m.booking from Make m where m.customer = :customer")
	List<Booking> findAllBookingByCustomer(@Param("customer") Customer customer);

}

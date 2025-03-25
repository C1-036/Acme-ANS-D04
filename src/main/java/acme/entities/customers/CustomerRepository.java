
package acme.entities.customers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("Select Count(c) > 0 From Customer c where c.identifier = :identifier")
	boolean existsByIdentifier(String identifier);

	@Query("Select c from Customer c where c.identifier= :identifier")
	Customer findByIdentifier(String identifier);

	@Query("Select c from Customer c where c.id = :id")
	Customer findById(int id);

}

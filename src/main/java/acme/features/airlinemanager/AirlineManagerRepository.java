/*
 * AirlineManagerRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airlinemanager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerRepository extends AbstractRepository {

	@Query("SELECT COUNT(am) > 0 FROM AirlineManager am WHERE am.identifierNumber = :identifierNumber")
	boolean existsByIdentifierNumber(String identifierNumber);

	@Query("SELECT am FROM AirlineManager am WHERE am.identifierNumber = :identifierNumber")
	AirlineManager findByIdentifierNumber(String identifierNumber);

}

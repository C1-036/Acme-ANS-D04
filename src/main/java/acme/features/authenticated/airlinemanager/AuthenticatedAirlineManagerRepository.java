/*
 * AuthenticatedAirlineManagerRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.airlinemanager;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AuthenticatedAirlineManagerRepository extends AbstractRepository {

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id = :id")
	UserAccount findUserAccountById(@Param("id") int id);

	@Query("SELECT a FROM Airline a WHERE a.id = :id")
	Airline findAirlineById(@Param("id") int id);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirlines();
}

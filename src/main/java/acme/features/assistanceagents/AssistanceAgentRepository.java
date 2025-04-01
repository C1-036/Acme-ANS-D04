
package acme.features.assistanceagents;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT COUNT(aa) > 0 FROM AssistanceAgent aa WHERE aa.employeeCode = :employeeCode")
	boolean existsByEmployeeCode(String employeeCode);

	@Query("SELECT aa FROM AssistanceAgent aa WHERE aa.employeeCode = :employeeCode")
	AssistanceAgent findByEmployeeCode(String employeeCode);
}

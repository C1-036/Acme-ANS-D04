
package acme.features.assistanceagent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.AssistanceAgents;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT COUNT(aa) > 0 FROM AssistanceAgents aa WHERE aa.employeeCode = :employeeCode")
	boolean existsByEmployeeCode(String employeeCode);

	@Query("SELECT aa FROM AssistanceAgents aa WHERE aa.employeeCode = :employeeCode")
	AssistanceAgents findByEmployeeCode(String employeeCode);
}

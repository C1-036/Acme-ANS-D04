
package acme.features.assistanceagents.tracking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.Tracking;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM Tracking t WHERE t.claim.id = :claimId")
	Collection<Tracking> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT t FROM Tracking t WHERE t.id = :id")
	Tracking findTrackingLogById(int id);

}

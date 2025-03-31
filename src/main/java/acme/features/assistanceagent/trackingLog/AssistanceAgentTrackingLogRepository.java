
package acme.features.assistanceagent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.assistanceAgent.id = :assistanceAgentId")
	Collection<TrackingLog> findTrackingLogsByAssistanceAgentId(int assistanceAgentId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment DESC")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT COUNT(t) > 0 FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage = 100")
	boolean existsTrackingLogWithFullResolution(int claimId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :id")
	TrackingLog findTrackingLogById(int id);

}


package acme.features.assistanceagents.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assistanceagents.Claim;
import acme.entities.assistanceagents.Tracking;
import acme.entities.flights.Leg;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :id AND (c.accepted = acme.entities.assistanceagents.ClaimState.ACCEPTED OR c.accepted = acme.entities.assistanceagents.ClaimState.REJECTED)")
	Collection<Claim> findCompletedClaimsByAssistanceAgentId(int id);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :assistanceAgentId AND c.accepted = acme.entities.assistanceagents.ClaimState.PENDING")
	Collection<Claim> findUndergoingClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select a from AssistanceAgent a where a.id = :assistanceAgentId")
	AssistanceAgent findAssistanceAgentById(int assistanceAgentId);

	@Query("select l from Leg l where l.draftMode = true")
	Collection<Leg> findAllLegsNotPublished();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select t from Tracking t where t.claim.id = :claimId")
	Collection<Tracking> findAllTrackingLogsByClaimId(int claimId);

}

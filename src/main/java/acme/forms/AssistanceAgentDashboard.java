
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						ratioResolvedClaims;
	Double						ratioRejectedClaims;
	List<String>				topThreeMonthsWithHighestClaims;

	Double						averageLogsPerClaim;
	Integer						minLogsPerClaim;
	Integer						maxLogsPerClaim;
	Double						standardDeviationLogsPerClaim;

	Double						averageClaimsAssistedLastMonth;
	Integer						minClaimsAssistedLastMonth;
	Integer						maxClaimsAssistedLastMonth;
	Double						standardDeviationClaimsAssistedLastMonth;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}


package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						totalAirportsInternational;
	Integer						totalAirportsDomestic;
	Integer						totalAirportsRegional;

	Integer						totalAirlinesLuxury;
	Integer						totalAirlinesStandard;
	Integer						totalAirlinesLowCost;

	Double						ratioAirlinesWithEmailAndPhone;

	Double						ratioActiveAircrafts;
	Double						ratioNonActiveAircrafts;

	Double						ratioReviewsAboveFive;

	Integer						totalReviewsLastTenWeeks;
	Double						averageReviewsLastTenWeeks;
	Integer						minReviewsLastTenWeeks;
	Integer						maxReviewsLastTenWeeks;
	Double						standardDeviationReviewsLastTenWeeks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}

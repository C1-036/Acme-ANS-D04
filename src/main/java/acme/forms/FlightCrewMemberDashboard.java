
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				lastFiveDestinations;

	Integer						legsWithSeverity0To3;
	Integer						legsWithSeverity4To7;
	Integer						legsWithSeverity8To10;

	List<String>				lastLegCrewMembers;

	// Flight assignments grouped by status
	List<String>				flightAssignmentsByStatus;

	// Statistics for flight assignments in the last month
	Double						averageFlightAssignmentsLastMonth;
	Integer						minFlightAssignmentsLastMonth;
	Integer						maxFlightAssignmentsLastMonth;
	Double						standardDeviationFlightAssignmentsLastMonth;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}

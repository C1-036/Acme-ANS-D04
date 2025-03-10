
package acme.entities.flightCrewMembers;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Leg;
import acme.realms.FlightCrewMembers;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Automapped
	@Mandatory
	@Valid
	private FlightDuty			duty;

	@Automapped
	@Mandatory
	@ValidMoment(past = true)
	private Date				lastUpdate;

	@Automapped
	@Mandatory
	@Valid
	private AssignmentStatus	status;

	@Automapped
	@Optional
	@ValidString(max = 255)
	private String				remarks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	private FlightCrewMembers	flightCrewMember;

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	private Leg					flightLeg;
}

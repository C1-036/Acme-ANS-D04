
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAirlineManager;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAirlineManager
public class AirlineManager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private Integer				yearsOfExperience;

	@Mandatory
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = true)
	@Automapped
	private Date				dateOfBirth;

	@Optional
	@ValidUrl
	@Automapped
	private String				picture;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@ManyToOne(optional = false)
	@Mandatory
	@Automapped
	private Airline				airline;
}

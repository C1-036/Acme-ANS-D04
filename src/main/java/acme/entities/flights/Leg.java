
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{4}$")
	@Column(unique = true)
	@Automapped
	private String				flightNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	@ValidMoment
	@Automapped
	private Date				scheduledDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	@ValidMoment
	@Automapped
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber
	@Automapped
	private Double				durationHours;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@ValidString
	@Automapped
	private String				departureAirport;

	@Mandatory
	@ValidString
	@Automapped
	private String				arrivalAirport;

	@Mandatory
	@ValidString
	@Automapped
	private String				aircraft;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Flight				flight;
}

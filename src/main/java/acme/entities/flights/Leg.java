
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
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
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{4}$", message = "{validation.entities.leg.flightNumber}")
	private String				flightNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				scheduledDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				scheduledArrival;

	@Mandatory
	private Double				durationHours;

	@Mandatory
	private LegStatus			status;

	@Mandatory
	private String				departureAirport;

	@Mandatory
	private String				arrivalAirport;

	@Mandatory
	private String				aircraft;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Flight				flight;
}

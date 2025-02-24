
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(nullable = false)
	private String				tag;

	@Mandatory
	private Boolean				requiresSelfTransfer; // 

	@Mandatory
	@ValidMoney(min = 1.00, max = Double.POSITIVE_INFINITY)
	private Money				cost;

	@ValidString
	@Optional
	private String				description;

	// Derived attributes -----------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@PastOrPresent
	@Mandatory
	private Date				scheduledDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				scheduledArrival;

	@Mandatory
	private String				originCity;

	@Mandatory
	private String				destinationCity;

	@Mandatory
	private Integer				layovers;

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Manager				manager;

	@OneToMany
	@Mandatory
	@Valid
	private List<Leg>			legs;
}

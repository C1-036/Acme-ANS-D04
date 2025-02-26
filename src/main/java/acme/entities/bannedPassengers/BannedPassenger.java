
package acme.entities.bannedPassengers;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BannedPassenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				fullName;

	@Mandatory
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = true)
	@Automapped
	private Date				dateOfBirth;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Automapped
	private String				passportNumber;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				nationality;

	@Mandatory
	@ValidString(min = 1)
	@Automapped
	private String				banReason;

	@Mandatory
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = true)
	@Automapped
	private Date				banDate;

	@Optional
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = true)
	@Automapped
	private Date				liftDate;
}

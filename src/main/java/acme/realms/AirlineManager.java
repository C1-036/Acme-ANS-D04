
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Transient;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIdentifierNumber;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidIdentifierNumber
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 9, max = 8, pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	@Automapped
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 120, integer = 3, fraction = 0)
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

	// Relaciones
	@ManyToOne(optional = false)
	@Mandatory
	@Automapped
	private Airline				airline;


	@Transient
	public String getInitials() {
		DefaultUserIdentity identity = this.getUserAccount().getIdentity();
		String fullName = identity.getFullName();

		String[] parts = fullName.split(", ");
		String[] nameParts = parts[1].split(" ");

		StringBuilder initials = new StringBuilder();
		initials.append(parts[0].charAt(0));

		for (int i = 0; i < Math.min(nameParts.length, 2); i++)
			initials.append(nameParts[i].charAt(0));

		return initials.toString().toUpperCase();
	}
}

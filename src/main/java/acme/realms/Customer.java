
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidIdentifier;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidIdentifier
public class Customer extends AbstractRealm {

	private static final long	serialVersionUID	= 1L;

	//Atributes-----------------------------------

	@Mandatory
	@Valid
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(min = 0, max = 500000)
	@Automapped
	private Integer				earnedPoints;

}

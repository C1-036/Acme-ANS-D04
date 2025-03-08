
package acme.realms;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	private String				license;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phone;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				specialization;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				passedMedicalTest;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	@Automapped
	private Integer				yearsExperience;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				certifications;

}

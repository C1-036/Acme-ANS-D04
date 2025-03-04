
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractRealm;
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
public class AssistanceAgents extends AbstractRealm {

	private static final long	serialVersionUID	= 1L;

	//Atributes-----------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				spokenLanguages;

	@Mandatory
	@Automapped
	@ValidString
	private String				airline;

	@Mandatory
	@Automapped
	@ValidMoment
	private Date				startDate;

	@Optional
	@Automapped
	@ValidString(min = 255)
	private String				briefBio;

	@Optional
	@Automapped
	private Double				salary;

	@Optional
	@Automapped
	@ValidString
	private String				photoLink;

}

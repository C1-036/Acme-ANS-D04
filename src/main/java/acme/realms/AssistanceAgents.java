
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constrains.ValidEmployeeCode;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgents extends AbstractRealm {

	private static final long	serialVersionUID	= 1L;

	//Atributes-----------------------------------

	@Mandatory
	@ValidEmployeeCode
	@Automapped
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				spokenLanguages;

	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	private Date				moment;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				briefBio;

	@Optional
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidUrl
	private String				picture;

}

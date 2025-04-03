
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAssistanceAgent;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
public class AssistanceAgent extends AbstractRealm {

	private static final long	serialVersionUID	= 1L;

	//Atributes-----------------------------------

	@Mandatory
	@Valid
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				spokenLanguages;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
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

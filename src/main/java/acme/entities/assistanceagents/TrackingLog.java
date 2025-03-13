
package acme.entities.assistanceagents;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Atributos ------------------------------------
	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				stepUndergoing;

	@Mandatory
	@ValidNumber(min = 0, fraction = 2)
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				accepted;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				resolutionDetails;

	// Relationships ----------------------------------------------------------
	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Claim				claim;
}

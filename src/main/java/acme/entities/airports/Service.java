
package acme.entities.airports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromoCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Size(min = 1, max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				picture;

	@Mandatory
	@Min(0)
	@Automapped
	private Double				avgDwellTime;

	@Column(unique = true)
	@ValidPromoCode
	@Optional
	@Automapped
	private String				promoCode;

	@Optional
	@Min(0)
	@Max(100)
	@Automapped
	private Double				money;
}

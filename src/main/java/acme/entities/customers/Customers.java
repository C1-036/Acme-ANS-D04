
package acme.entities.customers;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customers extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Atributes-----------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")  // He tenido que usar \\ para que no me de error Esta bien??
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")  // Aqui igual
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	private String				physicalAddress;

	@Mandatory
	@ValidString(max = 50)
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	private String				country;

	@Optional
	@ValidNumber(max = 500000)
	private Integer				earnedPoints;

}

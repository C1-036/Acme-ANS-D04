
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskType			type;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	@Column(nullable = false)
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 0)
	@Automapped
	@Column(nullable = false)
	private int					priority;

	@Mandatory
	@ValidNumber(min = 0, integer = 2, fraction = 2)
	@Automapped
	@Column(nullable = false)
	private double				estimatedDuration;

	@Mandatory
	@Valid
	@ManyToOne
	@Automapped
	private MaintenanceRecord	maintenanceRecord;


	public enum TaskType {
		MAINTENANCE, INSPECTION, REPAIR, SYSTEM_CHECK
	}
}

package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskType			type;  // MAINTENANCE, INSPECTION, REPAIR, SYSTEM_CHECK

	@Column(length = 255, nullable = false)
	private String				description;

	@Min(0)
	@Max(10)
	@Column(nullable = false)
	private int					priority;  // Rango: 0-10

	@Min(0)
	@Digits(integer = 2, fraction = 2)
	@Column(nullable = false)
	private double				estimatedDuration;  // En horas

	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;


	public enum TaskType {
		MAINTENANCE, INSPECTION, REPAIR, SYSTEM_CHECK
	}
}

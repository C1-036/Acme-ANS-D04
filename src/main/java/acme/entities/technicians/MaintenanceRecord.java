
package acme.entities.technicians;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Column(nullable = false)
	private Date				moment;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status				status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.DATE)
	@Automapped
	@Column(nullable = false)
	private Date				nextInspection;

	@Mandatory
	@Min(0)
	@Automapped
	@Column(nullable = false)
	private double				estimatedCost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				notes;

	@Mandatory
	@Valid
	@ManyToOne
	@Automapped
	private Technician			technician;

	@Valid
	@OneToMany
	private List<Task>			tasks;


	public enum Status {
		PENDING, IN_PROGRESS, COMPLETED
	}
}

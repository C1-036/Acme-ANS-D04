package acme.realms;

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

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date				moment;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status				status;  // PENDING, IN_PROGRESS, COMPLETED

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date				nextInspection;

	@Column(nullable = false)
	private double				estimatedCost;

	@Column(length = 255)
	private String				notes;

	@ManyToOne(optional = false)
	private Technician			technician;

	@OneToMany(mappedBy = "maintenanceRecord")
	private List<Task>			tasks;

	public enum Status {
		PENDING, IN_PROGRESS, COMPLETED
	}
}

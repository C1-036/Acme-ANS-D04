package acme.realms;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	@Column(unique = true, nullable = false)
	private String					license;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	@Column(nullable = false)
	private String					phone;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	@Column(nullable = false)
	private String					specialization;

	@Mandatory
	@Automapped
	@Column(nullable = false)
	private boolean					passedMedicalTest;

	@Mandatory
	@ValidNumber(min = 0, max = 100, integer = 3, fraction = 0)
	@Automapped
	@Column(nullable = false)
	private int						yearsExperience;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String					certifications;

	@Valid
	@OneToMany(mappedBy = "technician")
	private List<MaintenanceRecord>	maintenanceRecords;

}
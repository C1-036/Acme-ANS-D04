package acme.realms;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true, nullable = false)
	private String					license;  // Formato "^[A-Z]{2-3}\\d{6}$"

	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Column(nullable = false)
	private String					phone;  // Formato "^\+?\d{6,15}$"

	@Column(length = 50, nullable = false)
	private String					specialisation;

	@Column(nullable = false)
	private boolean					passedMedicalTest;

	@Column(nullable = false)
	private int						yearsExperience;

	@Column(length = 255)
	private String					certifications;

	@OneToMany(mappedBy = "technician")
	private List<MaintenanceRecord>	maintenanceRecords;

}

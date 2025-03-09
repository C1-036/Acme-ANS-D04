
package acme.forms;

import java.io.Serializable;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm implements Serializable {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<String, Long>		maintenanceRecordsByStatus;  // Número de registros de mantenimiento por estado
	private String					nextInspectionDue;  // Inspección más próxima en la que participa
	private Map<String, Integer>	topFiveAircraftsByTasks;  // Top 5 aeronaves con más tareas

	private Double					avgEstimatedCost;
	private Double					minEstimatedCost;
	private Double					maxEstimatedCost;
	private Double					stdDevEstimatedCost;

	private Double					avgEstimatedDuration;
	private Double					minEstimatedDuration;
	private Double					maxEstimatedDuration;
	private Double					stdDevEstimatedDuration;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}

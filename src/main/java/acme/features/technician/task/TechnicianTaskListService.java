
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.technicians.MaintenanceRecord;
import acme.entities.technicians.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		Integer masterId;
		Boolean mine;
		MaintenanceRecord maintenanceRecord;
		int technicianId;

		// Obtener parámetros de la petición de forma segura
		masterId = super.getRequest().hasData("masterId") ? super.getRequest().getData("masterId", int.class) : null;
		mine = super.getRequest().hasData("mine") ? super.getRequest().getData("mine", boolean.class) : false;
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (masterId != null) {
			maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
			status = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());
		} else
			status = true;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Collection<Task> tasks;
		Integer masterId;
		Boolean mine;
		int technicianId;

		masterId = super.getRequest().hasData("masterId") ? super.getRequest().getData("masterId", int.class) : null;
		mine = super.getRequest().hasData("mine") ? super.getRequest().getData("mine", boolean.class) : false;
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (masterId != null)
			tasks = this.repository.findTasksByMasterId(masterId);
		else if (mine)
			tasks = this.repository.findTasksByTechnicianId(technicianId);
		else
			tasks = this.repository.findPublishedTasks();

		super.getBuffer().addData(tasks);

	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "description");
		super.addPayload(dataset, task, "estimatedDurationHours");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Task> tasks) {
		Integer masterId;
		MaintenanceRecord maintenanceRecord;
		boolean showCreate;
		boolean mine;

		masterId = super.getRequest().hasData("masterId") ? super.getRequest().getData("masterId", int.class) : null;
		mine = super.getRequest().hasData("mine") ? super.getRequest().getData("mine", boolean.class) : false;

		if (masterId != null) {
			maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
			showCreate = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());
		} else
			showCreate = false;

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("mine", mine);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}

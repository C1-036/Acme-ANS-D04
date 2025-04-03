
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.technicians.Involves;
import acme.entities.technicians.MaintenanceRecord;
import acme.entities.technicians.Task;
import acme.entities.technicians.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task task;
		Technician technician;
		Integer maintenanceRecordId = null;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setDraftMode(true);
		task.setTechnician(technician);

		if (super.getRequest().hasData("maintenanceRecordId"))
			try {
				maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", Integer.class);
			} catch (Throwable oops) {
				maintenanceRecordId = null;
			}

		super.getBuffer().addData(task);
		super.getBuffer().addGlobal("maintenanceRecordId", maintenanceRecordId); // 💥
	}

	@Override
	public void bind(final Task task) {

		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(task, "type", "description", "priority", "estimatedDurationHours");

		task.setTechnician(technician);
	}

	@Override
	public void validate(final Task task) {

		if (!super.getBuffer().getErrors().hasErrors("estimatedDurationHours"))
			super.state(task.getEstimatedDurationHours() > 0, "estimatedDurationHours", "acme.validation.technician.task.positive-duration");
		if (!super.getBuffer().getErrors().hasErrors("priority")) {
			boolean validPriority = task.getPriority() >= 0 && task.getPriority() <= 10;
			super.state(validPriority, "priority", "acme.validation.technician.task.priority-out-of-range");
		}

	}

	@Override
	public void perform(final Task task) {
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		Involves involves = new Involves();

		maintenanceRecordId = super.getRequest().hasData("maintenanceRecordId") ?//
			super.getRequest().getData("maintenanceRecordId", Integer.class) : null;

		this.repository.save(task);

		if (maintenanceRecordId != null) {

			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

			involves.setTask(task);
			involves.setMaintenanceRecord(maintenanceRecord);

			this.repository.save(involves);
		}
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDurationHours", "draftMode");

		String technicianName = task.getTechnician() != null && task.getTechnician().getIdentity() != null ? task.getTechnician().getIdentity().getFullName() : "";
		dataset.put("technician", technicianName);
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		Integer maintenanceRecordId = super.getBuffer().getGlobal("maintenanceRecordId", Integer.class);
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}

}


package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.technicians.Task;
import acme.entities.technicians.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		Integer taskId;
		Task task;
		Technician technician;

		if (super.getRequest().hasData("id", Integer.class)) {
			taskId = super.getRequest().getData("id", Integer.class);
			if (taskId != null) {
				task = this.repository.findTaskById(taskId);
				if (task != null) {
					technician = task.getTechnician();
					status = task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);
				}
			}
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {

		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(task, "type", "description", "priority", "estimatedDurationHours");

		task.setTechnician(technician);
	}

	@Override
	public void validate(final Task task) {
		if (!super.getBuffer().getErrors().hasErrors("priority")) {
			boolean validPriority = task.getPriority() >= 0 && task.getPriority() <= 10;
			super.state(validPriority, "priority", "acme.validation.technician.task.priority-out-of-range");
		}
		if (!super.getBuffer().getErrors().hasErrors("estimatedDurationHours")) {
			boolean validDuration = task.getEstimatedDurationHours() >= 0 && task.getEstimatedDurationHours() <= 1000;
			super.state(validDuration, "estimatedDurationHours", "acme.validation.technician.task.positive-duration");
		}
		if (!super.getBuffer().getErrors().hasErrors("description")) {
			boolean validDescription = task.getDescription() != null && task.getDescription().length() <= 255;
			super.state(validDescription, "description", "acme.validation.technician.task.description-too-long");
		}
	}

	@Override
	public void perform(final Task task) {
		task.setDraftMode(false);
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDurationHours", "draftMode");
		dataset.put("technician", task.getTechnician().getIdentity().getFullName());
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}

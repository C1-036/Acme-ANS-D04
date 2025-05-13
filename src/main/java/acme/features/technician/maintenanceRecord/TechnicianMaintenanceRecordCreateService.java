
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.technicians.MaintenanceRecord;
import acme.entities.technicians.MaintenanceStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		Integer maintenanceRecordId = null;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		if (super.getRequest().hasData("maintenanceRecordId")) {
			maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", Integer.class);

			if (maintenanceRecordId != null) {
				maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

				if (maintenanceRecord != null) {
					technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
					status = maintenanceRecord.isDraftMode() && technician.equals(maintenanceRecord.getTechnician());
				}
			}
		} else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setTechnician(technician);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {

		//Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes");

		//maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setAircraft(super.getRequest().getData("aircraft", Aircraft.class));
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		if (!super.getBuffer().getErrors().hasErrors("moment") && !super.getBuffer().getErrors().hasErrors("inspectionDueDate")) {
			boolean momentBeforeInspection = maintenanceRecord.getMoment().before(maintenanceRecord.getInspectionDueDate());
			super.state(momentBeforeInspection, "inspectionDueDate", "acme.validation.technician.maintenance-record.moment-before-inspection");
		}

		if (!super.getBuffer().getErrors().hasErrors("status")) {
			boolean validStatus = !(maintenanceRecord.isDraftMode() && maintenanceRecord.getStatus().equals(MaintenanceStatus.COMPLETED));
			super.state(validStatus, "status", "acme.validation.technician.maintenance-record.completed-in-draft");
		}

		//if (!super.getBuffer().getErrors().hasErrors("estimatedCost")) {
		//boolean positiveCost = maintenanceRecord.getEstimatedCost().getAmount() != null && maintenanceRecord.getEstimatedCost().getAmount() > 0;
		//super.state(positiveCost, "estimatedCost", "acme.validation.technician.maintenance-record.positive-cost");
		//}

	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Collection<Aircraft> aircrafts;
		SelectChoices choicesAircrafts;
		SelectChoices choicesStatus;
		Dataset dataset;

		aircrafts = this.repository.findAircrafts();

		choicesStatus = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());
		choicesAircrafts = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());
		dataset.put("aircraft", choicesAircrafts.getSelected().getKey());
		dataset.put("aircrafts", choicesAircrafts);
		dataset.put("status", choicesStatus.getSelected().getKey());
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}

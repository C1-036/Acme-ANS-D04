
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


	/*
	 * @Override
	 * public void authorise() {
	 * boolean status = true;
	 * int aircraftId;
	 * Aircraft aircraft;
	 * 
	 * if (super.getRequest().hasData("aircraft", int.class)) {
	 * aircraftId = super.getRequest().getData("aircraft", int.class);
	 * aircraft = this.repository.findAircraftById(aircraftId);
	 * 
	 * if (aircraft == null && aircraftId != 0)
	 * status = false;
	 * }
	 * 
	 * super.getResponse().setAuthorised(status);
	 * }
	 */
	@Override
	public void authorise() {
		boolean status = true;

		// Comprobar que el usuario tiene rol de Technician
		Technician tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(tech);

		// Si es POST y hay campo aircraft, verificar que el avión existe
		if (super.getRequest().getMethod().equals("POST") && super.getRequest().hasData("aircraft", int.class)) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			if (aircraftId != 0 && this.repository.findAircraftById(aircraftId) == null)
				status = false;
		}

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

		super.bindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes");

		maintenanceRecord.setAircraft(super.getRequest().getData("aircraft", Aircraft.class));

	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
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

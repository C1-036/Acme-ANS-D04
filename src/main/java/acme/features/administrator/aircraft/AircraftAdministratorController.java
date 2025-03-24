
package acme.features.administrator.aircraft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.aircraft.Aircraft;

@GuiController
public class AircraftAdministratorController extends AbstractGuiController<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AircraftAdministratorListService	listService;

	@Autowired
	private AircraftAdministratorShowService	showService;

	@Autowired
	private AircraftAdministratorDeleteService	deleteService;

	@Autowired
	private AircraftAdministratorCreateService	createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		//super.addBasicCommand("update", this.updateService);

	}

}

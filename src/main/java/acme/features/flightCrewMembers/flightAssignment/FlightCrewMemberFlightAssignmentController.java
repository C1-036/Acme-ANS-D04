
package acme.features.flightCrewMembers.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightCrewMembers.FlightAssignment;
import acme.realms.FlightCrewMembers;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentListLegsCompletedService	listCompletedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListLegsPlannedService		listPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService					showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService				createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService				updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService				publishService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService				deleteService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}

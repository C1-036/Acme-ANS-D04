
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
	private FlightCrewMemberFlightAssignmentUpdateService				updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService				createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService				deleteService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService				publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}

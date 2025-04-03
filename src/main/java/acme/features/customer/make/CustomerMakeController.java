
package acme.features.customer.make;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.customers.Make;
import acme.realms.Customer;

@GuiController
public class CustomerMakeController extends AbstractGuiController<Customer, Make> {

	@Autowired
	private CustomerMakeCreateService	createService;

	@Autowired
	private CustomerMakeDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}


package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.customers.Booking;
import acme.entities.customers.TravelClass;

public class Customerdashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	//Atributes

	Integer						destinations;
	Money						spentDestinations;
	Map<TravelClass, Booking>	numberOfBookings;
	Money						countCost;
	Money						averageCost;
	Money						minCost;
	Money						maxCost;
	Money						desviationCost;
	Integer						countPassenger;
	Integer						averagePassenger;
	Integer						minPassenger;
	Integer						maxPassenger;
	Integer						desviationPassenger;

}

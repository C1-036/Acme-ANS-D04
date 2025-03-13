
package acme.entities.flights;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Indication			indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Date departure = repository.findScheduledDeparture(this.getId());
		Date now = MomentHelper.getCurrentMoment(); // Fecha actual del sistema
		Date maxDepartureDate = MomentHelper.parse("2200/12/31 23:58:00", "yyyy/MM/dd HH:mm:ss"); // Última salida posible

		// Validar que la fecha de salida esté en el futuro (mínimo: CURRENT MOMENT)
		if (departure != null && MomentHelper.isBefore(departure, now))
			departure = null;

		// Validar que la fecha de salida no supere el máximo permitido (2200/12/31 23:58:00)
		if (departure != null && MomentHelper.isAfter(departure, maxDepartureDate))
			departure = null;

		return departure;
	}

	@Transient
	public Date getScheduledArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Date arrival = repository.findScheduledArrival(this.getId());
		Date departure = this.getScheduledDeparture();
		Date maxArrivalDate = MomentHelper.parse("2201/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss");

		if (departure == null)
			return null; // Si no hay salida programada, no puede haber llegada.

		// Calculamos la llegada mínima: salida + 1 minuto
		Date minArrival = MomentHelper.deltaFromMoment(departure, 1, ChronoUnit.MINUTES);

		// Si no hay llegada programada o si es menor a minArrival, forzamos minArrival
		if (arrival == null || MomentHelper.isBefore(arrival, minArrival))
			arrival = minArrival;

		// Validar que la llegada no supere el máximo permitido (2201/01/01 00:00:00)
		if (MomentHelper.isAfter(arrival, maxArrivalDate))
			arrival = null;

		return arrival;
	}

	@Transient
	public String getOriginCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findOriginCity(this.getId());
	}

	@Transient
	public String getDestinationCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findDestinationCity(this.getId());
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Integer legs = repository.countLegs(this.getId());
		return legs != null && legs > 0 ? legs - 1 : 0;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager airlinemanager;

}

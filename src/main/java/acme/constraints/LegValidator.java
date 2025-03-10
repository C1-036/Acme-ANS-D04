
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightRepository;
import acme.entities.flights.Leg;

public class LegValidator implements ConstraintValidator<ValidLeg, Leg> {

	@Override
	public void initialize(final ValidLeg annotation) {
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		if (leg == null || leg.getFlight() == null || leg.getScheduledDeparture() == null || leg.getScheduledArrival() == null)
			return false;

		Flight flight = leg.getFlight();
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> allLegs = repository.findLegsByFlight(flight.getId());

		//Validar que el Leg no se solape con otro Leg del mismo vuelo
		for (Leg otherLeg : allLegs) {
			if (Objects.equals(otherLeg.getId(), leg.getId()))
				continue; // No compararse consigo mismo

			if (MomentHelper.isInRange(leg.getScheduledDeparture(), otherLeg.getScheduledDeparture(), otherLeg.getScheduledArrival())) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("acme.validation.leg.valid-leg.overlap").addConstraintViolation();
				return false;
			}
		}

		//Validar que scheduledArrival ≥ scheduledDeparture + delta (1 minuto)
		if (MomentHelper.isBefore(leg.getScheduledArrival(), MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES))) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.leg.valid-leg.time-conflict").addConstraintViolation();
			return false;
		}

		return true;
	}
}

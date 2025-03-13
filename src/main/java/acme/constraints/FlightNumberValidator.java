
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.FlightRepository;
import acme.entities.flights.Leg;

@Validator
public class FlightNumberValidator extends AbstractValidator<ValidFlightNumber, Leg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private FlightRepository repository;


	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidFlightNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			String flightNumber = leg.getFlightNumber();

			{ // Validación del formato del número de vuelo
				String airlineIataCode = null;
				if (leg.getFlight() != null && leg.getFlight().getAirlinemanager() != null && leg.getFlight().getAirlinemanager().getAirline() != null)
					airlineIataCode = leg.getFlight().getAirlinemanager().getAirline().getIataCode();

				boolean validFormat = false;
				if (flightNumber != null && airlineIataCode != null) {
					String pattern = "^" + airlineIataCode + "\\d{4}$";
					validFormat = flightNumber.matches(pattern);
				}

				super.state(context, validFormat, "flightNumber", "acme.validation.leg.flight-number.format.message");

				if (!validFormat)
					result = false;
			}

			{ // Validación de unicidad del número de vuelo
				Leg existingLeg = this.repository.findLegByFlightNumber(flightNumber);
				boolean uniqueFlightNumber = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.leg.flight-number.duplicate.message");

				if (!uniqueFlightNumber)
					result = false;
			}
		}

		return result;
	}
}

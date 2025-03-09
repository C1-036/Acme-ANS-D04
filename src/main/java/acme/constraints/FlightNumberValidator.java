
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Leg;

@Validator
public class FlightNumberValidator extends AbstractValidator<ValidFlightNumber, Leg> {

	@Override
	protected void initialise(final ValidFlightNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null || leg.getFlightNumber() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else if (leg.getFlight() == null || leg.getFlight().getAirlinemanager() == null || leg.getFlight().getAirlinemanager().getAirline() == null || leg.getFlight().getAirlinemanager().getAirline().getIataCode() == null) {

			super.state(context, false, "flightNumber", "acme.validation.leg.flight-number.missing-airline.message");
			result = false;
		} else {
			String airlineIataCode = leg.getFlight().getAirlinemanager().getAirline().getIataCode();
			boolean startsWithIata = leg.getFlightNumber().startsWith(airlineIataCode);

			super.state(context, startsWithIata, "flightNumber", "acme.validation.leg.flight-number.mismatch.message");

			if (!startsWithIata)
				result = false;
		}

		return result;
	}
}


package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.entities.flights.Leg;

public class FlightNumberValidator implements ConstraintValidator<ValidFlightNumber, Leg> {

	private static final String FLIGHT_NUMBER_PATTERN = "^[A-Z]{3}\\d{4}$";  // IATA Code + 4 dígitos


	@Override
	public void initialize(final ValidFlightNumber annotation) {
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		if (leg == null || leg.getFlightNumber() == null)
			return false;  // No puede ser nulo

		String flightNumber = leg.getFlightNumber();

		// Validar que el formato del flightNumber es correcto (IATA_CODE + 4 dígitos)
		if (!Pattern.matches(FlightNumberValidator.FLIGHT_NUMBER_PATTERN, flightNumber)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("El flightNumber debe seguir el formato IATA_CODE + 4 dígitos. Ejemplo: 'MAD1234'").addConstraintViolation();
			return false;
		}

		// Obtener el IATA Code desde el Departure Airport
		if (leg.getDepartureAirport() == null || leg.getDepartureAirport().getIataCode() == null)
			return true; // No hay aeropuerto de salida, no podemos validar

		String iataCode = leg.getDepartureAirport().getIataCode(); // 🔹 Ahora sí obtenemos el código IATA correcto

		// Validar que el flightNumber empieza con el código IATA del Departure Airport
		if (!flightNumber.startsWith(iataCode)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("El flightNumber debe empezar con el código IATA del aeropuerto de salida: " + iataCode).addConstraintViolation();
			return false;
		}

		return true;
	}
}

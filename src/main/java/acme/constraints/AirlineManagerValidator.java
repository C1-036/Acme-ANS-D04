
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.realms.AirlineManager;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (airlineManager == null || airlineManager.getIdentifierNumber() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			DefaultUserIdentity identity = airlineManager.getUserAccount().getIdentity();
			if (identity == null || identity.getFullName() == null) {
				super.state(context, false, "identifierNumber", "acme.validation.airline-manager.identifier-number.missing-name.message");
				result = false;
			} else {
				String fullName = identity.getFullName();
				String[] parts = fullName.split(", ");
				if (parts.length < 2) {
					super.state(context, false, "identifierNumber", "acme.validation.airline-manager.identifier-number.invalid-format.message");
					result = false;
				} else {
					String surname = parts[0];
					String[] nameParts = parts[1].split(" ");

					// Generar iniciales permitiendo 2 o 3 letras
					StringBuilder expectedInitials = new StringBuilder();
					expectedInitials.append(Character.toUpperCase(surname.charAt(0)));
					for (int i = 0; i < Math.min(nameParts.length, 2); i++)
						expectedInitials.append(Character.toUpperCase(nameParts[i].charAt(0)));

					String identifier = airlineManager.getIdentifierNumber();
					String actualInitials = identifier.substring(0, expectedInitials.length());

					boolean isValid = actualInitials.equals(expectedInitials.toString());

					super.state(context, isValid, "identifierNumber", "acme.validation.airline-manager.identifier-number.mismatch.message");

					if (!isValid)
						result = false;
				}
			}
		}

		Date birthDate = airlineManager.getDateOfBirth();
		if (birthDate == null) {
			super.state(context, false, "dateOfBirth", "acme.validation.airline-manager.date-of-birth.required");
			result = false;
		} else {
			// Calculamos la fecha mínima para ser mayor de 18 años usando `MomentHelper`
			Date minimumValidDate = MomentHelper.deltaFromCurrentMoment(-18, ChronoUnit.YEARS);

			// Verificamos que la fecha de nacimiento esté antes de la fecha mínima
			boolean isAdult = MomentHelper.isBeforeOrEqual(birthDate, minimumValidDate);

			if (!isAdult) {
				super.state(context, false, "dateOfBirth", "acme.validation.airline-manager.date-of-birth.must-be-adult");
				result = false;
			}
		}

		return result;
	}
}

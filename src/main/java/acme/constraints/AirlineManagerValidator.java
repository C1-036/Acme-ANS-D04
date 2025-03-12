
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

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airlineManager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{ // Validación del nombre y del identificador
				DefaultUserIdentity identity = airlineManager.getIdentity();
				boolean validIdentity = identity != null && identity.getFullName() != null;

				super.state(context, validIdentity, "identifierNumber", "acme.validation.airline-manager.identifier-number.missing-name.message");

				if (validIdentity) {
					String fullName = identity.getFullName().trim();
					String[] nameParts = fullName.split(" ", 2);

					boolean hasSurname = nameParts.length == 2;
					super.state(context, hasSurname, "identifierNumber", "acme.validation.airline-manager.identifier-number.invalid-format.message");

					if (hasSurname) {
						String firstName = nameParts[0];
						String lastName = nameParts[1];

						char firstInitial = Character.toUpperCase(firstName.charAt(0));
						char lastInitial = Character.toUpperCase(lastName.charAt(0));

						String expectedPrefix = "" + firstInitial + lastInitial;

						if (lastName.contains(" ")) {
							char secondLastInitial = Character.toUpperCase(lastName.split(" ")[1].charAt(0));
							expectedPrefix += secondLastInitial;
						}

						String identifier = airlineManager.getIdentifierNumber().toUpperCase();

						boolean validIdentifier = identifier.matches("^[A-Z]{2,3}\\d{6}$") && identifier.startsWith(expectedPrefix);

						super.state(context, validIdentifier, "identifierNumber", "acme.validation.airline-manager.identifier-number.mismatch.message");
					}
				}
			}
			{ // Validación de la fecha de nacimiento (debe ser mayor de 18 años)
				Date birthDate = airlineManager.getDateOfBirth();
				boolean validBirthDate = birthDate != null;

				super.state(context, validBirthDate, "dateOfBirth", "acme.validation.airline-manager.date-of-birth.required");

				if (validBirthDate) {
					Date minimumValidDate = MomentHelper.deltaFromCurrentMoment(-18, ChronoUnit.YEARS);
					boolean isAdult = MomentHelper.isBeforeOrEqual(birthDate, minimumValidDate);

					super.state(context, isAdult, "dateOfBirth", "acme.validation.airline-manager.date-of-birth.must-be-adult");
				}
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}

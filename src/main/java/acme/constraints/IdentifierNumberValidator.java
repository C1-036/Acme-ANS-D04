
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AirlineManager;

@Validator
public class IdentifierNumberValidator extends AbstractValidator<ValidIdentifierNumber, AirlineManager> {

	@Override
	protected void initialise(final ValidIdentifierNumber annotation) {
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
				super.state(context, false, "identifierNumber", "acme.validation.identifier-number.missing-name.message");
				result = false;
			} else {
				String fullName = identity.getFullName();
				String[] parts = fullName.split(", ");
				if (parts.length < 2) {
					super.state(context, false, "identifierNumber", "acme.validation.identifier-number.invalid-format.message");
					result = false;
				} else {
					String surname = parts[0];
					String[] nameParts = parts[1].split(" ");

					// Generar iniciales permitiendo 2 o 3 letras
					StringBuilder expectedInitials = new StringBuilder();
					expectedInitials.append(Character.toUpperCase(surname.charAt(0))); // Primera letra del apellido
					for (int i = 0; i < Math.min(nameParts.length, 2); i++)
						expectedInitials.append(Character.toUpperCase(nameParts[i].charAt(0))); // Hasta 2 letras del nombre

					String identifier = airlineManager.getIdentifierNumber();
					String actualInitials = identifier.substring(0, expectedInitials.length()); // Extraemos las iniciales del ID

					boolean isValid = actualInitials.equals(expectedInitials.toString());

					super.state(context, isValid, "identifierNumber", "acme.validation.identifier-number.message");

					if (!isValid)
						result = false;
				}
			}
		}

		return result;
	}
}

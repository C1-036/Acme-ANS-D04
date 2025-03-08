
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
					String firstName = parts[1].split(" ")[0];

					char expectedFirstLetter = Character.toUpperCase(firstName.charAt(0));
					char expectedSecondLetter = Character.toUpperCase(surname.charAt(0));

					String identifier = airlineManager.getIdentifierNumber();
					char actualFirstLetter = Character.toUpperCase(identifier.charAt(0));
					char actualSecondLetter = Character.toUpperCase(identifier.charAt(1));

					boolean isValid = actualFirstLetter == expectedFirstLetter && actualSecondLetter == expectedSecondLetter;

					super.state(context, isValid, "identifierNumber", "acme.validation.identifier-number.message");

					if (!isValid)
						result = false;
				}
			}
		}

		return result;
	}
}

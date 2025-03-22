
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class IdentifierValidator extends AbstractValidator<ValidIdentifier, Customer> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidIdentifier annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			DefaultUserIdentity identity = customer.getIdentity();
			boolean validIdentity = identity != null && identity.getFullName() != null;

			super.state(context, validIdentity, "identifier", "acme.validation.customer.identifier.missing-name.message");

			if (validIdentity) {
				String fullName = identity.getFullName().trim();
				String[] nameParts = fullName.split(" ", 2);

				boolean hasSurname = nameParts.length == 2;
				super.state(context, hasSurname, "identifier", "acme.validation.customer.identifier.invalid-format.message");

				if (hasSurname) {
					String firstName = nameParts[0];
					String lastName = nameParts[1];

					char firstInitial = firstName.charAt(0);
					char lastInitial = lastName.charAt(0);

					String expectedPrefix = "" + firstInitial + lastInitial;

					if (lastName.contains(" ")) {
						char secondLastInitial = lastName.split(" ")[1].charAt(0);
						expectedPrefix += secondLastInitial;
					}

					String identifier = customer.getIdentifier();

					boolean validIdentifier = identifier.matches("^[A-Z]{2,3}\\d{6}$") && identifier.startsWith(expectedPrefix);

					super.state(context, validIdentifier, "identifier", "acme.validation.customer.identifier.mismatch.message");

				}
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}

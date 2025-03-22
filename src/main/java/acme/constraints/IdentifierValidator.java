
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.customers.CustomerRepository;
import acme.realms.Customer;

@Validator
public class IdentifierValidator extends AbstractValidator<ValidIdentifier, Customer> {

	@Autowired
	private CustomerRepository repository;

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

			if (identity != null && identity.getName() != null && identity.getSurname() != null) {
				String name = identity.getName().trim();
				String surname = identity.getSurname().trim();

				char nameInitial = name.charAt(0);
				char surnameInitial = surname.split(" ")[0].charAt(0);

				String expectedPrefix = "" + nameInitial + surnameInitial;
				String identifier = customer.getIdentifier();

				boolean matchesPattern = identifier.matches("^[A-Z]{2,3}\\d{6}$");
				boolean startWithPrefix = identifier.startsWith(expectedPrefix);

				boolean alreadyExists = this.repository.existsByIdentifier(identifier);
				Customer existing = this.repository.findByIdentifier(identifier);

				boolean isUnique = !alreadyExists || existing != null && existing.getId() == customer.getId();

				boolean isValidIdentifier = matchesPattern && startWithPrefix && isUnique;

				super.state(context, isValidIdentifier, "identifier", "acme.validation.customer.identifier.invalid.message");
			}
		}
		result = !super.hasErrors(context);
		return result;

	}
}

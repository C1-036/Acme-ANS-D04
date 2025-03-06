
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.realms.AirlineManager;

public class IdentifierNumberValidator implements ConstraintValidator<ValidIdentifierNumber, AirlineManager> {

	@Override
	public void initialize(final ValidIdentifierNumber annotation) {
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		if (airlineManager == null || airlineManager.getIdentifierNumber() == null)
			return false;  // Si no hay datos, no es válido

		// Obtener la identidad del usuario
		DefaultUserIdentity identity = airlineManager.getUserAccount().getIdentity();
		if (identity == null || identity.getFullName() == null)
			return false;  // Si no hay identidad, no se puede validar

		// Separar el nombre y apellido
		String fullName = identity.getFullName();
		String[] parts = fullName.split(", ");
		if (parts.length < 2)
			return false;  // Si no hay suficiente información, no es válido

		String surname = parts[0]; // Apellido
		String firstName = parts[1].split(" ")[0]; // Primer nombre

		// Obtener las primeras letras
		char expectedFirstLetter = Character.toUpperCase(firstName.charAt(0));
		char expectedSecondLetter = Character.toUpperCase(surname.charAt(0));

		// Obtener las letras del identificador
		String identifier = airlineManager.getIdentifierNumber();
		char actualFirstLetter = Character.toUpperCase(identifier.charAt(0));
		char actualSecondLetter = Character.toUpperCase(identifier.charAt(1));

		// Validar que las letras coincidan
		boolean isValid = actualFirstLetter == expectedFirstLetter && actualSecondLetter == expectedSecondLetter;

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Las primeras letras del identificador deben coincidir con el nombre y apellido.").addConstraintViolation();
		}

		return isValid;
	}
}
